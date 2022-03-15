package dbhelper.query

import dbhelper.query.fields.Fields.Companion.wrap
import dbhelper.query.conditions.Condition
import dbhelper.query.fields.Fields
import dbhelper.query.join.*
import dbhelper.query.order.Order
import dbhelper.query.where.EmptyWhere
import dbhelper.query.where.GenericWhere
import dbhelper.query.where.Where

class QueryBuilder {
    private var from: From? = null
    private var fields: Fields = Fields.all()
    private var where: Where = EmptyWhere()
    private var joins: Joins = Joins()
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
        this.from = From(table)
        return this
    }

    fun fields(vararg fields: String): QueryBuilder {
        this.fields = Fields(fields.toList())
        return this
    }

    fun where(condition: Condition): QueryBuilder {
        where = GenericWhere(condition)
        return this
    }

    fun join(on: On): QueryBuilder {
        joins.join(on)
        return this
    }
    
    fun leftJoin(on: On): QueryBuilder {
        joins.leftJoin(on)
        return this
    }

    fun rightJoin(on: On): QueryBuilder {
        joins.rightJoin(on)
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

        if (from == null) {
            throw RuntimeException("Cannot generate query without table name")
        }

        return Query("SELECT $fields$from${joins.statement()}$where$groupBy$having$orderBy$limit", where.params().plus(havingParams))
    }

    companion object {
        @JvmStatic
        fun raw(statement: String, vararg params: Any?) = QueryBuilder().raw(statement, *params).build()
    }
}
