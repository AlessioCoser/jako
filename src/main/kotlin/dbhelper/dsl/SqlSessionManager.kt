package dbhelper.dsl

import java.sql.DriverManager

class SqlSessionManager(private val jdbc: String, private val user: String, private val password: String): SessionManager {
    override fun <T> session(fn: SqlSession.() -> T): T {
        return DriverManager.getConnection(jdbc, user, password).use {
            fn(SqlSession(it))
        }
    }
}
