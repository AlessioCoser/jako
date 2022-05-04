package jako.database

import java.sql.Connection
import java.sql.DriverManager

class SimpleConnector(private val jdbcConnectionString: String): DatabaseConnector {
    override fun connection(): Connection {
        return DriverManager.getConnection(jdbcConnectionString)
    }
}
