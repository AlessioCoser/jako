package dbhelper.database

import java.io.Closeable
import java.sql.Connection

interface Transaction : Closeable {
    val connection: Connection
    fun commit()
    fun rollback()
    override fun close()
}
