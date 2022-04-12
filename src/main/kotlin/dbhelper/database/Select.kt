package dbhelper.database

import dbhelper.RowSql
import dbhelper.query.Query
import dbhelper.query.Row
import dbhelper.query.RowParser

class Select internal constructor(private val transactionManager: TransactionManager, private val query: Query) {
    fun <T> all(parser: RowParser<T>): List<T> {
        return all { parser.parse(this) }
    }

    fun <T> all(parseRow: RowSql.() -> T): List<T> {
        return transactionManager.useConnection {
            val resultSet = it.prepareStatement(query).executeQuery()
            val items: MutableList<T> = ArrayList()
            while (resultSet.next()) {
                items.add(parseRow(RowSql(resultSet)))
            }
            items
        }
    }

    fun <T> first(parser: RowParser<T>): T? {
        return first { parser.parse(this) }
    }

    fun <T> first(parseRow: Row.() -> T): T? {
        return transactionManager.useConnection {
            val resultSet = it.prepareStatement(query).executeQuery()

            if (resultSet.next()) {
                parseRow(RowSql(resultSet))
            } else {
                null
            }
        }
    }
}
