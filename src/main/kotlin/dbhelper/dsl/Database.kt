package dbhelper.dsl

import dbhelper.dsl.query.QueryBuilder
import dbhelper.dsl.query.QueryBuilderSql
import dbhelper.dsl.query.QueryExecutor

class Database(jdbc: String, user: String, password: String) {
    private val manager = SessionManagerSql(jdbc, user, password)

    fun select(prepare: QueryBuilderSql.() -> Unit): QueryExecutor {
        val queryBuilder = QueryBuilderSql()
        prepare(queryBuilder)
        return select(queryBuilder)
    }

    fun select(queryBuilder: QueryBuilder): QueryExecutor {
        return QueryExecutor(manager, queryBuilder)
    }
}
