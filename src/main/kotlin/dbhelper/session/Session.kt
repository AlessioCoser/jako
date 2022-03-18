package dbhelper.session

import dbhelper.RowSql
import dbhelper.query.Query
import dbhelper.query.Row
import java.sql.Connection
import java.sql.PreparedStatement

class Session(private val connection: Connection) {
    fun <T> execute(query: Query, parseRow: Row.() -> T): List<T> {
        val resultSet = connection.prepareStatement(query.statement)
            .setParameters(*query.params.toTypedArray())
            .executeQuery()

        val results = mutableListOf<T>()
        while (resultSet.next()) {
            results.add(parseRow(RowSql(resultSet)))
        }

        return results.toList()
    }

    private fun PreparedStatement.setParameters(vararg params: Any?): PreparedStatement {
        params.forEachIndexed { index, param -> setObject(index + 1, param) }
        return this
    }
}