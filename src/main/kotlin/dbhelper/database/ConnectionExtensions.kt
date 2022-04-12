package dbhelper.database

import dbhelper.dsl.Statement
import java.sql.Connection
import java.sql.PreparedStatement

fun Connection.prepareStatement(query: Statement) = prepareStatement(query.statement, query.params)

fun Connection.prepareStatement(sql: String, parameters: List<Any?>): PreparedStatement {
    val prepareStatement = prepareStatement(sql)
    parameters.forEachIndexed { index, param -> prepareStatement.setObject(index + 1, param) }
    return prepareStatement
}