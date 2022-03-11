package dbhelper.dsl

class Database(private val manager: ConnectionManager) {
    fun select(prepare: QueryBuilder.() -> Unit): QueryExecutor {
        val queryBuilder = QueryBuilder()
        prepare(queryBuilder)
        return QueryExecutor(manager, queryBuilder)
    }

    companion object {
        @JvmStatic
        fun connect(): Database {
            return Database(ConnectionManager("jdbc:postgresql://localhost:5432/tests", "user", "password"))
        }
    }
}