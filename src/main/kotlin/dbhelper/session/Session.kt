package dbhelper.session

import dbhelper.insert.Insert
import dbhelper.query.Query
import dbhelper.query.Row

interface Session {
    fun execute(insert: Insert)
    fun <T> execute(query: Query, parseRow: Row.() -> T): List<T>
}
