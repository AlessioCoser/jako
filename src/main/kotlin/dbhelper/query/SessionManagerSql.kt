package dbhelper.query

import java.sql.DriverManager

class SessionManagerSql(private val jdbc: String, private val user: String, private val password: String):
    SessionManager {
    override fun <T> session(fn: SessionSql.() -> T): T {
        return DriverManager.getConnection(jdbc, user, password).use {
            fn(SessionSql(it))
        }
    }
}
