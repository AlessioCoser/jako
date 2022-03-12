package dbhelper.query

import dbhelper.query.conditions.Condition
import dbhelper.query.conditions.True
import dbhelper.query.join.Join
import dbhelper.query.join.JoinBuilder
import dbhelper.query.order.Order

class QueryBuilderSql : QueryBuilder {
    private var from: String = ""
    private var fields: List<String> = listOf("*")
    private var where: Condition = True()
    private var joins: JoinBuilder = JoinBuilder()
    private var groupBy: String = ""
    private var having: Condition? = null
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

    fun having(condition: Condition): QueryBuilderSql {
        having = condition
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
        if (raw.isNotBlank()) {
            return Query(raw, emptyList())
        }

        return Query(
            "SELECT ${joinFields()}${fromBuilder()}${joins.build()} WHERE ${where.statement()}$groupBy${joinHaving()}$orderBy$limit",
            where.params().plus(havingParams())
        )
    }

    private fun fromBuilder(): String {
        if(from.isBlank()) {
            throw RuntimeException("Cannot generate query without table name")
        }

        return " FROM $from"
    }

    private fun havingParams() = having?.params() ?: emptyList()

    private fun joinHaving(): String {
        return having?.statement()?.prependIndent(" HAVING ") ?: ""
    }

    private fun joinFields(): String {
        return fields.joinToString(separator = ", ")
    }
}
