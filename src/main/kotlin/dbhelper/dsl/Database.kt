package dbhelper.dsl

class Database(private val manager: ConnectionManager) {
    fun select(prepare: Select2.() -> Unit): Query {
        val select = Select2()
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