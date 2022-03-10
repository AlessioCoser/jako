package dbhelper.dsl

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

class NewSelect<T> {
    private var from: String = ""
    private var fields: List<String> = listOf("*")
    var forEach: ResultSet.() -> T = { throw RuntimeException("Cannot parse $from records") }
    var onEmpty: () -> T = { throw RuntimeException("No records found in $from") }

    fun from(table: String) {
        this.from = table
    }

    fun select(vararg fields: String) {
        this.fields = fields.toList()
    }

    fun forEach(forEach: ResultSet.() -> T) {
        this.forEach = forEach
    }

    fun statement(): String {
        return "SELECT ${joinFields()} FROM $from"
    }

    private fun joinFields(): String {
        return fields.joinToString(separator = ", ")
    }
}

class Database(private val manager: ConnectionManager) {

    fun <T> all(prepare: NewSelect<T>.() -> Unit): List<T> {
        val select = NewSelect<T>()
        prepare(select)
        println("select = $select")

        return manager.connection {
            val resultSet = prepareStatement(select.statement()).executeQuery()
            val results = mutableListOf<T>()
            while (resultSet.next()) {
                results.add(select.forEach(resultSet))
            }

            results.toList()
        }
    }

    fun <T> first(prepare: NewSelect<T>.() -> Unit): T {
        val select = NewSelect<T>()
        prepare(select)
        println("select = $select")

        return manager.connection {
            val resultSet = prepareStatement(select.statement()).executeQuery()
            if (resultSet.next()) select.forEach(resultSet) else select.onEmpty()
        }
    }

    companion object {
        @JvmStatic
        fun connect() = Database(ConnectionManager("jdbc:postgresql://localhost:5432/tests", "user", "password"))
    }
}

class ConnectionManager(private val jdbc: String, private val user: String, private val password: String) {
    fun <T> connection(fn: Connection.() -> T): T {
        return DriverManager.getConnection(jdbc, user, password).use {
            fn(it)
        }
    }
}