package dbhelper.dsl

import dbhelper.setParameters
import java.sql.ResultSet

class Query(private val manager: ConnectionManager, private val select: Select2) {

    fun <T> all(forEach: ResultSet.() -> T): List<T> {
        println("$select")
        return manager.connection {
            val resultSet = prepareStatement(select.build())
                .setParameters(*select.params().toTypedArray())
                .executeQuery()
            val results = mutableListOf<T>()
            while (resultSet.next()) {
                results.add(forEach(resultSet))
            }

            results.toList()
        }
    }

    fun <T> first(forEach: ResultSet.() -> T): T {
        println("$select")
        return manager.connection {
            val resultSet = prepareStatement(select.build())
                .setParameters(*select.params().toTypedArray())
                .executeQuery()
            if (resultSet.next()) forEach(resultSet) else throw RuntimeException("No records found for: $select")
        }
    }
}
