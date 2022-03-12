package dbhelper.query

class Database(jdbc: String, user: String, password: String) {
    private val manager = SessionManagerSql(jdbc, user, password)

    fun select(prepare: dbhelper.query.QueryBuilderSql.() -> Unit): QueryExecutor {
        val queryBuilder = dbhelper.query.QueryBuilderSql()
        prepare(queryBuilder)
        return select(queryBuilder)
    }

    fun select(queryBuilder: dbhelper.query.QueryBuilder): QueryExecutor {
        return QueryExecutor(manager, queryBuilder)
    }
}
