package dbhelper.dsl

import dbhelper.dsl.query.Query
import dbhelper.dsl.query.Row

interface Session {
    fun <T> execute(query: Query, parseRow: Row.() -> T): List<T>
}