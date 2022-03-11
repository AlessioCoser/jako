package dbhelper.dsl

import java.sql.Connection
import java.sql.PreparedStatement

class Session(connection: Connection): Connection by connection {
    fun <T> execute(query: Query, parseRow: Row.() -> T): List<T> {
        println(query)
        val resultSet = prepareStatement(query.statement)
            .setParameters(*query.params.toTypedArray())
            .executeQuery()

        val results = mutableListOf<T>()
        while (resultSet.next()) {
            results.add(parseRow(Row(resultSet)))
        }

        return results.toList()
    }

    private fun PreparedStatement.setParameters(vararg params: Any?): PreparedStatement {
        params.forEachIndexed { index, param -> setObject(index + 1, param) }
        return this
    }
}
