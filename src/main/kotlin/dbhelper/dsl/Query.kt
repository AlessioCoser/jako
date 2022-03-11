package dbhelper.dsl

import dbhelper.setParameters
import java.sql.ResultSet

class Query(private val manager: ConnectionManager, private val selectBuilder: SelectBuilder) {

    fun <T> all(forEach: ResultSet.() -> T): List<T> {
        val select = selectBuilder.build()
        println(select)
        return manager.connection {
            val resultSet = prepareStatement(select.statement)
                .setParameters(*select.params.toTypedArray())
                .executeQuery()
            val results = mutableListOf<T>()
            while (resultSet.next()) {
                results.add(forEach(resultSet))
            }

            results.toList()
        }
    }

    fun <T> first(forEach: ResultSet.() -> T): T {
        val select = selectBuilder.build()
        println(select)
        return manager.connection {
            val resultSet = prepareStatement(select.statement)
                .setParameters(*select.params.toTypedArray())
                .executeQuery()
            if (resultSet.next()) forEach(resultSet) else throw RuntimeException("No records found for: $selectBuilder")
        }
    }
}
