package dbhelper.session

import dbhelper.RowSql
import dbhelper.Statement
import dbhelper.insert.Insert
import dbhelper.query.Query
import dbhelper.query.Row
import java.sql.Connection
import java.sql.PreparedStatement

class SessionSql(override val connection: Connection): Session {
    override fun execute(insert: Insert) {
        preparedStatement(insert).execute()
    }

    override fun <T> execute(query: Query, parseRow: Row.() -> T): List<T> {
        val resultSet = preparedStatement(query).executeQuery()

        val results = mutableListOf<T>()
        while (resultSet.next()) {
            results.add(parseRow(RowSql(resultSet)))
        }

        return results.toList()
    }

    private fun preparedStatement(statement: Statement): PreparedStatement {
        return connection.prepareStatement(statement.statement)
            .setParameters(*statement.params.toTypedArray())
    }

    private fun PreparedStatement.setParameters(vararg params: Any?): PreparedStatement {
        params.forEachIndexed { index, param -> setObject(index + 1, param) }
        return this
    }
}
