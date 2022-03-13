package dbhelper

import java.sql.DriverManager

class SessionManager(private val jdbc: String, private val user: String, private val password: String) {
    fun <T> session(fn: Session.() -> T): T {
        return DriverManager.getConnection(jdbc, user, password).use {
            fn(Session(it))
        }
    }
}
