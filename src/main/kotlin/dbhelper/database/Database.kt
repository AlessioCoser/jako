package dbhelper.database

import dbhelper.dsl.StatementBuilder
import dbhelper.dsl.insert.Insert
import dbhelper.dsl.query.Query
import dbhelper.dsl.update.Update


class Database(val transactionManager: TransactionManager) {
    inline fun <T> useTransaction(func: (Transaction) -> T): T {
        return transactionManager.useTransaction(func)
    }

    fun update(builder: Update): Execute {
        return Execute(transactionManager, builder.build())
    }

    fun update(fn: Update.() -> Update): Execute {
        return update(fn(Update()))
    }

    fun insert(builder: Insert): Execute {
        return Execute(transactionManager, builder.build())
    }

    fun insert(fn: Insert.() -> Insert): Execute {
        return insert(fn(Insert()))
    }

    fun select(statement: StatementBuilder): Select {
        return Select(transactionManager, statement.build())
    }

    fun select(fn: Query.() -> Query): Select {
        return select(fn(Query()))
    }

    companion object {
        @JvmStatic
        fun connect(connector: DatabaseConnector): Database {
            return Database(TransactionManager { connector.connection })
        }
    }
}
