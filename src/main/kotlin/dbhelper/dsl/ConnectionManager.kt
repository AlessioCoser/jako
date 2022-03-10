package dbhelper.dsl

import java.sql.Connection
import java.sql.DriverManager

class ConnectionManager(private val jdbc: String, private val user: String, private val password: String) {
    fun <T> connection(fn: Connection.() -> T): T {
        return DriverManager.getConnection(jdbc, user, password).use {
            fn(it)
        }
    }
}