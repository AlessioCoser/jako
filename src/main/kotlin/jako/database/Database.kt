package jako.database

import jako.dsl.Statement


class Database(val transactionManager: TransactionManager) {
    inline fun <T> useTransaction(func: (Transaction) -> T): T {
        return transactionManager.useTransaction(func)
    }

    fun execute(statement: Statement) {
        return Execute(transactionManager, statement).execute()
    }

    fun select(statement: Statement): Select {
        return Select(transactionManager, statement)
    }

    companion object {
        @JvmStatic
        fun connect(jdbcConnectionString: String): Database {
            return connect(SimpleConnector(jdbcConnectionString))
        }

        @JvmStatic
        fun connect(connector: DatabaseConnector): Database {
            return Database(TransactionManager { connector.connection() })
        }
    }
}
