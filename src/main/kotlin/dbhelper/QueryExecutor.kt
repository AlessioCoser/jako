package dbhelper

import dbhelper.query.QueryBuilder
import dbhelper.query.Row
import dbhelper.query.RowParser
import dbhelper.session.SessionManager

class QueryExecutor(private val manager: SessionManager, private val queryBuilder: QueryBuilder) {

    fun <T> all(parser: RowParser<T>): List<T> {
        return all { parser.parse(this) }
    }

    fun <T> all(parseRow: Row.() -> T): List<T> {
        val query = queryBuilder.build()
        return manager.session { execute(query, parseRow) }
    }

    fun <T> first(parser: RowParser<T>): T {
        return first { parser.parse(this) }
    }

    fun <T> first(parseRow: Row.() -> T): T {
        val query = queryBuilder.single().build()
        return manager.session {
            val rows = execute(query, parseRow)
            rows.first() ?: throw RuntimeException("No records found for: $query")
        }
    }
}
