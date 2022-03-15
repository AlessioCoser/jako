package dbhelper.query

import dbhelper.query.fields.Fields.Companion.wrap
import dbhelper.query.conditions.Condition
import dbhelper.query.fields.Fields
import dbhelper.query.join.*
import dbhelper.query.order.Order

class QueryBuilder {
    private var from: String = ""
    private var fields: String = "*"
    private var where: Where = EmptyWhere()
    private var join: String = ""
    private var groupBy: String = ""
    private var having: String = ""
    private var havingParams: List<Any?> = emptyList()
    private var orderBy: String = ""
    private var limit: String = ""
    private var raw: String = ""
    private var rawParams: List<Any?> = emptyList()

    fun raw(statement: String, vararg params: Any?): QueryBuilder {
        raw = statement
        rawParams = params.toList()
        return this
    }

    fun from(table: String): QueryBuilder {
        this.from = From(table).statement()
        return this
    }

    fun fields(vararg fields: String): QueryBuilder {
        this.fields = Fields(fields.toList()).statement()
        return this
    }

    fun where(condition: Condition): QueryBuilder {
        where = WhereStatement(condition)
        return this
    }

    fun join(on: On) = join(InnerJoin(on))
    fun leftJoin(on: On) = join(LeftJoin(on))
    fun rightJoin(on: On) = join(RightJoin(on))

    private fun join(join: Join): QueryBuilder {
        this.join += " ${join.statement()}"
        return this
    }

    fun orderBy(vararg order: Order): QueryBuilder {
        orderBy = " ORDER BY ${order.joinToString(", ") { it.statement() }}"
        return this
    }

    fun groupBy(vararg fields: String): QueryBuilder {
        groupBy = " GROUP BY ${fields.joinToString(", ") { it.wrap() }}"
        return this
    }

    fun having(condition: Condition): QueryBuilder {
        having = " HAVING ${condition.statement()}"
        havingParams = condition.params()
        return this
    }

    fun limit(limit: Int, offset: Int = 0): QueryBuilder {
        if (offset != 0) {
            this.limit = " LIMIT $limit OFFSET $offset"
        } else {
            this.limit = " LIMIT $limit"
        }
        return this
    }

    fun single() = limit(1)

    fun build(): Query {
        if (raw.isNotBlank()) {
            return Query(raw, rawParams)
        }

        if (from.isBlank()) {
            throw RuntimeException("Cannot generate query without table name")
        }

        return Query("SELECT $fields$from$join${where.statement()}$groupBy$having$orderBy$limit", where.params().plus(havingParams))
    }

    companion object {
        @JvmStatic
        fun raw(statement: String, vararg params: Any?) = QueryBuilder().raw(statement, *params).build()
    }
}
