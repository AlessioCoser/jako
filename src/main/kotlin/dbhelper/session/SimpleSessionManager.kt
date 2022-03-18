package dbhelper.session

import java.sql.DriverManager

class SimpleSessionManager(private val jdbc: String, private val user: String, private val password: String):
    SessionManager {
    override fun <T> session(fn: Session.() -> T): T {
        return DriverManager.getConnection(jdbc, user, password).use {
            fn(Session(it))
        }
    }
}
