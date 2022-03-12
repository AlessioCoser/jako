package dbhelper.dsl.query

import dbhelper.dsl.query.conditions.Condition
import dbhelper.dsl.query.conditions.True
import dbhelper.dsl.query.join.Join
import dbhelper.dsl.query.order.Order

class SqlQueryBuilder: QueryBuilder {
    private var from: String = ""
    private var fields: List<String> = listOf("*")
    private var where: Condition = True()
    private var joins: MutableList<Join> = mutableListOf()
    private var groupBy: String = ""
    private var orderBy: String = ""
    private var limit: String = ""
    private var raw: String = ""

    fun raw(statement: String): SqlQueryBuilder {
        raw = statement
        return this
    }

    fun from(table: String): SqlQueryBuilder {
        this.from = table
        return this
    }

    fun fields(vararg fields: String): SqlQueryBuilder {
        this.fields = fields.toList()
        return this
    }

    fun where(condition: Condition): SqlQueryBuilder {
        where = condition
        return this
    }

    fun join(join: Join): SqlQueryBuilder {
        joins.add(join)
        return this
    }

    fun orderBy(order: Order): SqlQueryBuilder {
        orderBy = " ORDER BY ${order.statement()} ${order.direction()}"
        return this
    }

    fun groupBy(vararg fields: String): SqlQueryBuilder {
        groupBy = " GROUP BY ${fields.joinToString(", ")}"
        return this
    }

    fun limit(field: Int): SqlQueryBuilder {
        limit = " LIMIT $field"
        return this
    }

    override fun single() = limit(1)

    override fun build(): Query {
        if (raw.isNotBlank()) {
            return Query(raw, emptyList())
        }
        return Query(
            "SELECT ${joinFields()} FROM $from${joinJoins()} WHERE ${where.statement()}$groupBy$orderBy$limit",
            where.params()
        )
    }

    private fun joinJoins(): String {
        return joins.joinToString(separator = " ", prefix = " ") { it.statement() }
    }

    private fun joinFields(): String {
        return fields.joinToString(separator = ", ")
    }
}
