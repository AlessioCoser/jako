package jako.database

import jako.dsl.Dialect
import jako.dsl.Statement
import java.sql.Connection
import java.sql.PreparedStatement

fun Connection.prepareStatement(statement: Statement, dialect: Dialect) = prepareStatement(statement.toSQL(dialect = dialect), statement.params())

fun Connection.prepareStatement(sql: String, parameters: List<Any?>): PreparedStatement {
    val prepareStatement = prepareStatement(sql)
    parameters.forEachIndexed { index, param -> prepareStatement.setObject(index + 1, param) }
    return prepareStatement
}
