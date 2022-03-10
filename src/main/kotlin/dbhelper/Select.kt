package dbhelper

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class Where(vararg val conditions: Condition) {
    fun text(): String {
        if(conditions.isEmpty()) {
            return ""
        }

        return "WHERE true ${conditions.joinToString(separator = " ") { "${it.type()} ${it.text}" }}"
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
        where: Where = Where(),
        orderBy: String? = null,
        onEmpty: () -> T = { throw RuntimeException("No records found in $table") }
): Select<T> = Select(this, table, where, orderBy, onEmpty)

class Select<T>(
        private val connection: Connection,
        private val table: String,
        private val where: Where = Where(),
        private val orderBy: String? = null,
        private val onEmpty: () -> T = { throw RuntimeException("No records found in $table") }
) {
    private var joins = mutableListOf<String>()
    private var limit: Int? = null
    private var joinsParams = mutableListOf<Any?>()
    private var leftJoins = mutableListOf<String>()
    private var params = mutableListOf<Any?>()
    private var fields: Array<out String> = arrayOf("*")

    fun fields(vararg values: String): Select<T> {
        fields = values
        return this
    }

    fun limit(value: Int): Select<T> {
        limit = value
        return this
    }

    fun join(join: String, vararg params: Any): Select<T> {
        joins.add(join)
        joinsParams = joinsParams.plus(params.toList()).toMutableList()
        return this
    }

    fun leftJoin(join: String): Select<T> {
        leftJoins.add(join)
        joinsParams = joinsParams.plus(params).toMutableList()
        return this
    }

    fun first(builder: (ResultSet) -> T): T {
        val resultSet = query(1)

        if (resultSet.next()) {
            return builder(resultSet)
        }

        return onEmpty()
    }

    fun all(builder: (ResultSet) -> T): List<T> {
        val resultSet = query(limit)

        val results = mutableListOf<T>()
        while (resultSet.next()) {
            results.add(builder(resultSet))
        }

        return results
    }

    private fun joinJoins(): String {
        if (joins.isEmpty()) {
            return ""
        }
        return joins.joinToString(separator = " JOIN ", prefix = "JOIN ")
    }

    private fun joinLeftJoins(): String {
        if (leftJoins.isEmpty()) {
            return ""
        }
        return leftJoins.joinToString(separator = " LEFT JOIN ", prefix = "LEFT JOIN ")
    }

    private fun joinFields(): String {
        return fields.joinToString(separator = ", ")
    }

    private fun query(limit: Int?): ResultSet {
        val query = """
            SELECT ${joinFields()} FROM $table ${joinJoins()} ${joinLeftJoins()} ${where.text()} ${orderByPart()} ${limitPart(limit)}
        """
        return connection.prepareStatement(query)
                .setParameters(*(joinsParams + where.params()).toTypedArray())
                .executeQuery()
    }

    private fun limitPart(limit: Int?): String {
        if(limit == null) {
            return ""
        }
        return "LIMIT $limit"
    }

    private fun orderByPart(): String {
        if(orderBy.isNullOrBlank()) {
            return ""
        }

        return "ORDER BY $orderBy"
    }
}

fun PreparedStatement.setParameters(vararg params: Any?): PreparedStatement {
    params.forEachIndexed { index, param -> setObject(index + 1, param) }
    return this
}