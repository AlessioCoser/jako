package dbhelper

import dbhelper.query.QueryBuilder
import dbhelper.query.QueryBuilderSql

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
