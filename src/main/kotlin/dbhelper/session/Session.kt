package dbhelper.session

import dbhelper.insert.Insert
import dbhelper.query.Query
import dbhelper.query.Row
import java.sql.Connection

interface Session {
    val connection: Connection
    fun execute(insert: Insert)
    fun <T> execute(query: Query, parseRow: Row.() -> T): List<T>
}
