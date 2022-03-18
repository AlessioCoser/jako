package dbhelper

import dbhelper.query.QueryBuilder
import dbhelper.session.SessionManager
import dbhelper.session.SimpleSessionManager

class Database(private val manager: SessionManager) {
    constructor(jdbc: String, user: String, password: String): this(SimpleSessionManager(jdbc, user, password))

    fun select(prepare: QueryBuilder.() -> Unit): QueryExecutor {
        val queryBuilder = QueryBuilder()
        prepare(queryBuilder)
        return select(queryBuilder)
    }

    fun select(queryBuilder: QueryBuilder): QueryExecutor {
        return QueryExecutor(manager, queryBuilder)
    }
}
