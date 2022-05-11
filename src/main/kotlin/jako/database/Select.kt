package jako.database

import jako.dsl.Row
import jako.dsl.RowParser
import jako.dsl.Statement

class Select internal constructor(private val transactionManager: TransactionManager, private val statement: Statement) {
    fun <T> all(parser: RowParser<T>): List<T> {
        return all { parser.parse(this) }
    }

    fun <T> all(parseRow: RowSql.() -> T): List<T> {
        return transactionManager.useConnection {
            val resultSet = it.prepareStatement(statement).executeQuery()
            val items: MutableList<T> = mutableListOf()
            while (resultSet.next()) {
                items.add(parseRow(RowSql(resultSet)))
            }
            items.toList()
        }
    }

    fun <T> first(parser: RowParser<T>): T? {
        return first { parser.parse(this) }
    }

    fun <T> first(parseRow: Row.() -> T): T? {
        return transactionManager.useConnection {
            val resultSet = it.prepareStatement(statement).executeQuery()

            if (resultSet.next()) {
                parseRow(RowSql(resultSet))
            } else {
                null
            }
        }
    }
}
