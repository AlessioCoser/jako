package jako.database

import java.sql.Connection

interface DatabaseConnector {
    fun connection(): Connection
}