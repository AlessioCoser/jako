package dbhelper.database

import java.sql.Connection
import java.sql.DriverManager

class SimpleConnector(private val jdbcPostgresConnection: JdbcPostgresConnection): DatabaseConnector {
    override val connection: Connection
        get() = DriverManager.getConnection(jdbcPostgresConnection.connection)
}
