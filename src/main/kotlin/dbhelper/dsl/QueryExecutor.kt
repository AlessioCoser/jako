package dbhelper.dsl

import dbhelper.setParameters
import java.sql.ResultSet

class QueryExecutor(private val manager: ConnectionManager, private val queryBuilder: QueryBuilder) {

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
