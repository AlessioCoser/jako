package dbhelper

import com.zaxxer.hikari.HikariDataSource

class SessionManager(jdbc: String, user: String, password: String) {
    private val dataSource = HikariDataSource()

    init {
        dataSource.jdbcUrl = jdbc
        dataSource.username = user
        dataSource.password = password
        // dataSource.maximumPoolSize // start with this: ((2 * core_count) + number_of_disks)
    }

    fun <T> session(fn: Session.() -> T): T {
        return dataSource.connection.use {
            fn(Session(it))
        }
    }
}
