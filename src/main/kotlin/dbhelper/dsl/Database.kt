package dbhelper.dsl

class Database(private val manager: ConnectionManager) {
    fun select(prepare: Query.Builder.() -> Unit): QueryExecutor {
        val queryBuilder = Query.Builder()
        prepare(queryBuilder)
        return select(queryBuilder)
    }

    fun select(queryBuilder: Query.Builder): QueryExecutor {
        return QueryExecutor(manager, queryBuilder)
    }

    companion object {
        @JvmStatic
        fun connect(): Database {
            return Database(ConnectionManager("jdbc:postgresql://localhost:5432/tests", "user", "password"))
        }
    }
}