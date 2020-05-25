package dbhelper

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class Select<T>(private val connection: Connection, private val table: String) {
    private val joins = mutableListOf<String>()
    private var joinsParams = mutableListOf<Any?>()
    private val leftJoins = mutableListOf<String>()
    private var whereCondition = "true"
    private val andConditions = mutableListOf<String>()
    private var orderBy: String = ""
    private var params = mutableListOf<Any?>()
    private var fields: Array<out String> = arrayOf("*")
    private var onEmpty: () -> T = { throw RuntimeException("No records found in $table") }

    fun fields(vararg values: String): Select<T> {
        fields = values
        return this
    }

    fun orderBy(value: String): Select<T> {
        orderBy = "ORDER BY $value"
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

    fun where(condition: String, vararg params: Any): Select<T> {
        this.whereCondition = condition
        this.params = params.toMutableList()
        return this
    }

    fun and(condition: String, vararg params: Any?): Select<T> {
        this.andConditions.add(condition)
        this.params = this.params.plus(params.toList()).toMutableList()
        return this
    }

    fun onEmpty(doSomething: () -> T): Select<T> {
        this.onEmpty = doSomething
        return this
    }

    fun first(builder: (ResultSet) -> T): T {
        val resultSet = query("LIMIT 1")

        if (resultSet.next()) {
            return builder(resultSet)
        }

        return onEmpty()
    }

    fun all(builder: (ResultSet) -> T): List<T> {
        val resultSet = query()

        val results = mutableListOf<T>()
        while (resultSet.next()) {
            results.add(builder(resultSet))
        }

        return results
    }

    private fun joinAnds(): String {
        if (andConditions.isEmpty()) {
            return ""
        }
        return andConditions.joinToString(separator = " AND ", prefix = "AND ")
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

    private fun query(limit: String = ""): ResultSet {
        val query = """
            SELECT ${joinFields()} FROM $table ${joinJoins()} ${joinLeftJoins()} WHERE $whereCondition ${joinAnds()} $orderBy $limit
        """
        return connection.prepareStatement(query)
                .setParameters(*(joinsParams + params).toTypedArray())
                .executeQuery()
    }
}

fun PreparedStatement.setParameters(vararg params: Any?): PreparedStatement {
    params.forEachIndexed { index, param -> setObject(index + 1, param) }
    return this
}