package dbhelper

import dbhelper.query.QueryBuilder

class Database(jdbc: String, user: String, password: String) {
    private val manager = SessionManager(jdbc, user, password)

    fun select(prepare: QueryBuilder.() -> Unit): QueryExecutor {
        val queryBuilder = QueryBuilder()
        prepare(queryBuilder)
        return select(queryBuilder)
    }

    fun select(queryBuilder: QueryBuilder): QueryExecutor {
        return QueryExecutor(manager, queryBuilder)
    }
}
