package dbhelper.dsl

import dbhelper.setParameters
import java.sql.ResultSet

interface QueryResultParser<T> {
    fun parse(resultSet: ResultSet): T
}

class QueryExecutor(private val manager: ConnectionManager, private val queryBuilder: QueryBuilder) {

    fun <T> all(parser: QueryResultParser<T>): List<T> {
        return all { parser.parse(this) }
    }

    fun <T> all(forEach: ResultSet.() -> T): List<T> {
        val query = queryBuilder.build()
        println(query)
        return manager.connection {
            val resultSet = prepareStatement(query.statement)
                .setParameters(*query.params.toTypedArray())
                .executeQuery()
            val results = mutableListOf<T>()
            while (resultSet.next()) {
                results.add(forEach(resultSet))
            }

            results.toList()
        }
    }

    fun <T> first(parser: QueryResultParser<T>): T {
        return first { parser.parse(this) }
    }

    fun <T> first(forEach: ResultSet.() -> T): T {
        val query = queryBuilder.build()
        println(query)
        return manager.connection {
            val resultSet = prepareStatement(query.statement)
                .setParameters(*query.params.toTypedArray())
                .executeQuery()
            if (resultSet.next()) forEach(resultSet) else throw RuntimeException("No records found for: $queryBuilder")
        }
    }
}
