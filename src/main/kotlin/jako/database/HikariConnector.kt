package jako.database

import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

class HikariConnector(jdbcConnection: JdbcConnection, connectionPoolSize: Int = 10): DatabaseConnector {
    private val dataSource = HikariDataSource()
    override val connection: Connection
        get() = dataSource.connection

    init {
        dataSource.driverClassName = "org.postgresql.Driver"
        dataSource.jdbcUrl = jdbcConnection.connection
        dataSource.maximumPoolSize = connectionPoolSize // start with this: ((2 * core_count) + number_of_disks)
    }
}
