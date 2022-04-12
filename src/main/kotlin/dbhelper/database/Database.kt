package dbhelper.database

import dbhelper.Statement
import dbhelper.insert.Insert
import dbhelper.insert.InsertBuilder
import dbhelper.query.QueryBuilder


class Database(val transactionManager: TransactionManager) {
    inline fun <T> useTransaction(func: (Transaction) -> T): T {
        return transactionManager.useTransaction(func)
    }

    fun insert(statement: InsertBuilder): Execute {
        return Execute(transactionManager, statement.build())
    }

    fun insert(fn: InsertBuilder.() -> InsertBuilder): Execute {
        return insert(fn(InsertBuilder()))
    }

    fun select(query: QueryBuilder): Select {
        return Select(transactionManager, query.build())
    }

    fun select(fn: QueryBuilder.() -> QueryBuilder): Select {
        return select(fn(QueryBuilder()))
    }

    companion object {
        @JvmStatic
        fun connect(connector: DatabaseConnector): Database {
            return Database(TransactionManager { connector.connection })
        }
    }
}
