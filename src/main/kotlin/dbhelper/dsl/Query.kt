package dbhelper.dsl

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

class Select2 {
    private var from: String = ""
    private var fields: List<String> = listOf("*")

    fun from(table: String) {
        this.from = table
    }

    fun fields(vararg fields: String) {
        this.fields = fields.toList()
    }

    fun build(): String {
        return "SELECT ${joinFields()} FROM $from"
    }

    private fun joinFields(): String {
        return fields.joinToString(separator = ", ")
    }

    override fun toString() = "Select2<${build()}>"
}

class Query(private val manager: ConnectionManager, private val select2: Select2) {

    fun <T> all(forEach: ResultSet.() -> T): List<T> {
        return manager.connection {
            val resultSet = prepareStatement(select2.build()).executeQuery()
            val results = mutableListOf<T>()
            while (resultSet.next()) {
                results.add(forEach(resultSet))
            }

            results.toList()
        }
    }

    fun <T> first(forEach: ResultSet.() -> T): T {
        return manager.connection {
            val resultSet = prepareStatement(select2.build()).executeQuery()
            if (resultSet.next()) forEach(resultSet) else throw RuntimeException("No records found for: $select2")
        }
    }
}

class Database(private val manager: ConnectionManager) {
    fun select(prepare: Select2.() -> Unit): Query {
        val select = Select2()
        prepare(select)
        return Query(manager, select)
    }

    companion object {
        @JvmStatic
        fun connect(): Database {
            return Database(ConnectionManager("jdbc:postgresql://localhost:5432/tests", "user", "password"))
        }
    }
}

class ConnectionManager(private val jdbc: String, private val user: String, private val password: String) {
    fun <T> connection(fn: Connection.() -> T): T {
        return DriverManager.getConnection(jdbc, user, password).use {
            fn(it)
        }
    }
}