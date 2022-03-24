package dbhelper.session

import java.sql.DriverManager
import javax.sql.DataSource

class SimpleSessionManager(private val jdbc: String, private val user: String, private val password: String): SessionManager {
    override val dataSource: DataSource
        get() = throw NotImplementedError()

    override fun <T> session(fn: Session.() -> T): T {
        return DriverManager.getConnection(jdbc, user, password).use {
            fn(SessionSql(it))
        }
    }
}
