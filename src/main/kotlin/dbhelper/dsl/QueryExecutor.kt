package dbhelper.dsl

class QueryExecutor(private val manager: SessionManager, private val queryBuilder: Query.Builder) {

    fun <T> all(parser: QueryRowParser<T>): List<T> {
        return all { parser.parse(this) }
    }

    fun <T> all(parseRow: Row.() -> T): List<T> {
        val query = queryBuilder.build()
        return manager.session { execute(query, parseRow) }
    }

    fun <T> first(parser: QueryRowParser<T>): T {
        return first { parser.parse(this) }
    }

    fun <T> first(parseRow: Row.() -> T): T {
        val query = queryBuilder.limit(1).build()
        return manager.session {
            val rows = execute(query, parseRow)
            rows.first() ?: throw RuntimeException("No records found for: $query")
        }
    }
}
