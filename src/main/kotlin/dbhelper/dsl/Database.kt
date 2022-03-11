package dbhelper.dsl

class Database(private val manager: ConnectionManager) {
    fun select(prepare: SelectBuilder.() -> Unit): Query {
        val select = SelectBuilder()
        prepare(select)
        return Query(manager, select)
    }

    companion object {
        @JvmStatic
        fun connect(): Database {
            return Database(ConnectionManager("jdbc:postgresql://localhost:5432/tests", "user", "password"))
        }
    }
}