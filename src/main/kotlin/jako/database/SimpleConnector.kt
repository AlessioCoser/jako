package jako.database

import java.sql.Connection
import java.sql.DriverManager

class SimpleConnector(private val jdbcPostgresConnection: JdbcConnection): DatabaseConnector {
    override val connection: Connection
        get() = DriverManager.getConnection(jdbcPostgresConnection.connection)
}
