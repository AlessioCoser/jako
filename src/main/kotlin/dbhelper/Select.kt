package dbhelper

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class Where(vararg val conditions: Condition) {
    fun text(): String {
        if(conditions.isEmpty()) {
            return ""
        }

        return " WHERE true ${conditions.joinToString(separator = " ") { "${it.type()} ${it.text}" }}"
    }

    fun params(): List<Any> {
        return conditions.flatMap { it.params.toList() }
    }
}

interface Condition {
    val text: String
    val params: Array<out Any>

    fun type(): String
}

class And(override val text: String, override vararg val params: Any): Condition {
    override fun type() = "AND"
}

fun <T> Connection.select(
    table: String,
    map: (ResultSet) -> T,
    fields: List<String> = listOf("*"),
    where: Where = Where(),
    limit: Int? = null,
    orderBy: String? = null,
    onEmpty: () -> T = { throw RuntimeException("No records found in $table") }
): Select<T> = Select(this, table, map, fields, where, limit, orderBy, onEmpty)

open class Join(val text: String) {
    open fun type() = "INNER JOIN"
}

class LeftJoin(text: String): Join(text) {
    override fun type() = "LEFT JOIN"
}

class Select<T>(
    private val connection: Connection,
    private val table: String,
    private val map: (ResultSet) -> T,
    private val fields: List<String> = listOf("*"),
    private val where: Where = Where(),
    private val limit: Int? = null,
    private val orderBy: String? = null,
    private val onEmpty: () -> T = { throw RuntimeException("No records found in $table") }
) {
    private var joins = mutableListOf<Join>()
    private var joinsParams = mutableListOf<Any?>()

    fun join(text: String): Select<T> {
        joins.add(Join(text))
        return this
    }

    fun leftJoin(join: String): Select<T> {
        joins.add(LeftJoin(join))
        return this
    }

    fun first(): T {
        val resultSet = query(1)

        if (resultSet.next()) {
            return map(resultSet)
        }

        return onEmpty()
    }

    fun all(): List<T> {
        val resultSet = query(limit)

        val results = mutableListOf<T>()
        while (resultSet.next()) {
            results.add(map(resultSet))
        }

        return results
    }

    private fun joinJoins(): String {
        if (joins.isEmpty()) {
            return ""
        }

        return joins.joinToString(separator = " ", prefix = " ") { "${it.type()} ${it.text}" }
    }

    private fun joinFields(): String {
        return fields.joinToString(separator = ", ")
    }

    private fun query(limit: Int?): ResultSet {
        val query = """
            SELECT ${joinFields()} FROM $table${joinJoins()}${where.text()}${orderByPart()}${limitPart(limit)}
        """
        println("query = $query")
        return connection.prepareStatement(query)
                .setParameters(*(joinsParams + where.params()).toTypedArray())
                .executeQuery()
    }

    private fun limitPart(limit: Int?): String {
        if(limit == null) {
            return ""
        }
        return " LIMIT $limit"
    }

    private fun orderByPart(): String {
        if(orderBy.isNullOrBlank()) {
            return ""
        }

        return " ORDER BY $orderBy"
    }
}

fun PreparedStatement.setParameters(vararg params: Any?): PreparedStatement {
    params.forEachIndexed { index, param -> setObject(index + 1, param) }
    return this
}