package dbhelper

import dbhelper.query.Query
import dbhelper.query.Row

interface Session {
    fun <T> execute(query: Query, parseRow: Row.() -> T): List<T>
}