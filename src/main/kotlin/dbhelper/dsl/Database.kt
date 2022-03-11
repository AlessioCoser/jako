package dbhelper.dsl

class Database(jdbc: String, user: String, password: String) {
    private val manager = SessionManager(jdbc, user, password)

    fun select(prepare: Query.Builder.() -> Unit): QueryExecutor {
        val queryBuilder = Query.Builder()
        prepare(queryBuilder)
        return select(queryBuilder)
    }

    fun select(queryBuilder: Query.Builder): QueryExecutor {
        return QueryExecutor(manager, queryBuilder)
    }
}