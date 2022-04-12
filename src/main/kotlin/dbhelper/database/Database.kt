package dbhelper.database

import dbhelper.dsl.insert.InsertBuilder
import dbhelper.dsl.query.QueryBuilder
import dbhelper.dsl.update.UpdateBuilder


class Database(val transactionManager: TransactionManager) {
    inline fun <T> useTransaction(func: (Transaction) -> T): T {
        return transactionManager.useTransaction(func)
    }

    fun update(statement: UpdateBuilder): Execute {
        return Execute(transactionManager, statement.build())
    }

    fun update(fn: UpdateBuilder.() -> UpdateBuilder): Execute {
        return update(fn(UpdateBuilder()))
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
