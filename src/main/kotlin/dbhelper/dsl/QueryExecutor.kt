package dbhelper.dsl

import dbhelper.setParameters
import java.sql.Connection
import java.sql.ResultSet

class QueryExecutor(private val manager: ConnectionManager, private val queryBuilder: Query.Builder) {

    fun <T> all(parser: QueryRowParser<T>): List<T> {
        return all { parser.parse(this) }
    }

    fun <T> all(parseRow: ResultSet.() -> T): List<T> {
        val query = queryBuilder.build()
        return manager.connection { execute(query, parseRow) }
    }

    fun <T> first(parser: QueryRowParser<T>): T {
        return first { parser.parse(this) }
    }

    fun <T> first(parseRow: ResultSet.() -> T): T {
        val query = queryBuilder.build()
        return manager.connection {
            val rows = execute(query, parseRow)
            rows.first() ?: throw RuntimeException("No records found for: $query")
        }
    }

    private fun <T> Connection.execute(query: Query, parseRow: ResultSet.() -> T): List<T> {
        println(query)
        val resultSet = prepareStatement(query.statement)
            .setParameters(*query.params.toTypedArray())
            .executeQuery()

        val results = mutableListOf<T>()
        while (resultSet.next()) {
            results.add(parseRow(resultSet))
        }

        return results.toList()
    }
}
