package dbhelper.query

import dbhelper.query.conditions.Condition
import dbhelper.query.conditions.True
import dbhelper.query.join.Join
import dbhelper.query.order.Order

class QueryBuilderSql : QueryBuilder {
    private var from: String = ""
    private var fields: List<String> = listOf("*")
    private var where: Condition = True()
    private var joins: MutableList<Join> = mutableListOf()
    private var groupBy: String = ""
    private var orderBy: String = ""
    private var limit: String = ""
    private var raw: String = ""

    fun raw(statement: String): QueryBuilderSql {
        raw = statement
        return this
    }

    fun from(table: String): QueryBuilderSql {
        this.from = table
        return this
    }

    fun fields(vararg fields: String): QueryBuilderSql {
        this.fields = fields.toList()
        return this
    }

    fun where(condition: Condition): QueryBuilderSql {
        where = condition
        return this
    }

    fun join(join: Join): QueryBuilderSql {
        joins.add(join)
        return this
    }

    fun orderBy(vararg order: Order): QueryBuilderSql {
        orderBy = " ORDER BY ${order.joinToString(", ") { it.statement() }}"
        return this
    }

    fun groupBy(vararg fields: String): QueryBuilderSql {
        groupBy = " GROUP BY ${fields.joinToString(", ")}"
        return this
    }

    fun limit(limit: Int, offset: Int = 0): QueryBuilderSql {
        if(offset != 0) {
            this.limit = " LIMIT $limit OFFSET $offset"
        } else {
            this.limit = " LIMIT $limit"
        }
        return this
    }

    override fun single() = limit(1)

    override fun build(): Query {
        if(from.isNullOrBlank()) {
            throw RuntimeException("Cannot generate query without table name")
        }
        if (raw.isNotBlank()) {
            return Query(raw, emptyList())
        }
        return Query(
            "SELECT ${joinFields()} FROM $from${joinJoins()} WHERE ${where.statement()}$groupBy$orderBy$limit",
            where.params()
        )
    }

    private fun joinJoins(): String {
        if(joins.isEmpty()) {
            return ""
        }
        return joins.joinToString(separator = " ", prefix = " ") { it.statement() }
    }

    private fun joinFields(): String {
        return fields.joinToString(separator = ", ")
    }
}
