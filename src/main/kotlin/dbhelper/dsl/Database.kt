package dbhelper.dsl

import dbhelper.dsl.query.QueryBuilder
import dbhelper.dsl.query.QueryExecutor
import dbhelper.dsl.query.SqlQueryBuilder

class Database(jdbc: String, user: String, password: String) {
    private val manager = SqlSessionManager(jdbc, user, password)

    fun select(prepare: SqlQueryBuilder.() -> Unit): QueryExecutor {
        val queryBuilder = SqlQueryBuilder()
        prepare(queryBuilder)
        return select(queryBuilder)
    }

    fun select(queryBuilder: QueryBuilder): QueryExecutor {
        return QueryExecutor(manager, queryBuilder)
    }
}
