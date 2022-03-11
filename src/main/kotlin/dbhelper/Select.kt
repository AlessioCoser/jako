package dbhelper

import dbhelper.dsl.Empty
import dbhelper.dsl.WhereCondition
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

fun <T> Connection.select(
    table: String,
    map: (ResultSet) -> T,
    fields: List<String> = listOf("*"),
    where: WhereCondition = Empty(),
    joins: Joins = Joins(),
    limit: Int? = null,
    orderBy: String? = null,
    onEmpty: () -> T = { throw RuntimeException("No records found in $table") }
): Select<T> = Select(this, table, map, fields, where, joins, limit, orderBy, onEmpty)

class Select<T>(
    private val connection: Connection,
    private val table: String,
    private val forEach: (ResultSet) -> T,
    private val fields: List<String> = listOf("*"),
    private val where: WhereCondition = Empty(),
    private val joins: Joins = Joins(),
    private val limit: Int? = null,
    private val orderBy: String? = null,
    private val onEmpty: () -> T = { throw RuntimeException("No records found in $table") }
) {
    fun first(): T {
        val resultSet = query(1)

        if (resultSet.next()) {
            return forEach(resultSet)
        }

        return onEmpty()
    }

    fun all(): List<T> {
        val resultSet = query(limit)

        val results = mutableListOf<T>()
        while (resultSet.next()) {
            results.add(forEach(resultSet))
        }

        return results
    }

    private fun joinFields(): String {
        return fields.joinToString(separator = ", ")
    }

    private fun query(limit: Int?): ResultSet {
        val whereStatement = " WHERE ${where.statement()}"
        val query = """
            SELECT ${joinFields()} FROM $table${joins.text()}$whereStatement${orderByPart()}${limitPart(limit)}
        """
        println("query = $query")
        return connection.prepareStatement(query)
            .setParameters(*(joins.params()).toTypedArray())
            .executeQuery()
    }

    private fun limitPart(limit: Int?): String {
        if (limit == null) {
            return ""
        }
        return " LIMIT $limit"
    }

    private fun orderByPart(): String {
        if (orderBy.isNullOrBlank()) {
            return ""
        }

        return " ORDER BY $orderBy"
    }
}

fun PreparedStatement.setParameters(vararg params: Any?): PreparedStatement {
    params.forEachIndexed { index, param -> setObject(index + 1, param) }
    return this
}