package dbhelper.dsl

import dbhelper.dsl.query.Query
import dbhelper.dsl.query.Row
import dbhelper.dsl.query.SqlRow
import java.sql.Connection
import java.sql.PreparedStatement

class SqlSession(private val connection: Connection): Session {
    override fun <T> execute(query: Query, parseRow: Row.() -> T): List<T> {
        println(query)
        val resultSet = connection.prepareStatement(query.statement)
            .setParameters(*query.params.toTypedArray())
            .executeQuery()

        val results = mutableListOf<T>()
        while (resultSet.next()) {
            results.add(parseRow(SqlRow(resultSet)))
        }

        return results.toList()
    }

    private fun PreparedStatement.setParameters(vararg params: Any?): PreparedStatement {
        params.forEachIndexed { index, param -> setObject(index + 1, param) }
        return this
    }
}
