package jako.database

import java.sql.Connection

interface DatabaseConnector {
    val connection: Connection
}