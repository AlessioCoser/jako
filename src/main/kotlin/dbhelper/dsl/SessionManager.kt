package dbhelper.dsl

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class SessionManager(private val jdbc: String, private val user: String, private val password: String) {
    fun <T> session(fn: Session.() -> T): T {
        return DriverManager.getConnection(jdbc, user, password).use {
            fn(Session(it))
        }
    }
}

class Session(private val connection: Connection): Connection by connection {
    fun <T> execute(query: Query, parseRow: ResultSet.() -> T): List<T> {
        println(query)
        val resultSet = prepareStatement(query.statement)
            .setParameters(*query.params.toTypedArray())
            .executeQuery()

        val results = mutableListOf<T>()
        while (resultSet.next()) {
            results.add(parseRow(resultSet))
        }

        return results.toList()
    }

    private fun PreparedStatement.setParameters(vararg params: Any?): PreparedStatement {
        params.forEachIndexed { index, param -> setObject(index + 1, param) }
        return this
    }
}