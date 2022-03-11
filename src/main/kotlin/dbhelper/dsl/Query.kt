package dbhelper.dsl

import java.sql.ResultSet

class Query(private val manager: ConnectionManager, private val select2: Select2) {

    fun <T> all(forEach: ResultSet.() -> T): List<T> {
        return manager.connection {
            val resultSet = prepareStatement(select2.build()).executeQuery()
            val results = mutableListOf<T>()
            while (resultSet.next()) {
                results.add(forEach(resultSet))
            }

            results.toList()
        }
    }

    fun <T> first(forEach: ResultSet.() -> T): T {
        return manager.connection {
            val select = select2.build()
            println("select = $select")
            val resultSet = prepareStatement(select).executeQuery()
            if (resultSet.next()) forEach(resultSet) else throw RuntimeException("No records found for: $select2")
        }
    }
}