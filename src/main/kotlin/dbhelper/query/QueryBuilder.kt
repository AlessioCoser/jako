package dbhelper.query

import dbhelper.query.fields.Fields.Companion.wrap
import dbhelper.query.conditions.Condition
import dbhelper.query.fields.Fields
import dbhelper.query.having.EmptyHaving
import dbhelper.query.having.GenericHaving
import dbhelper.query.having.Having
import dbhelper.query.join.*
import dbhelper.query.order.NoOrder
import dbhelper.query.order.Order
import dbhelper.query.order.OrderBy
import dbhelper.query.order.OrderField
import dbhelper.query.where.*

class QueryBuilder {
    private var rawQuery: Query? = null
    private var from: From? = null
    private var fields: Fields = Fields.all()
    private var where: Where = EmptyWhere()
    private var joins: Joins = Joins()
    private var having: Having = EmptyHaving()
    private var groupBy: String = ""
    private var orderBy: Order = NoOrder()
    private var limit: String = ""

    fun raw(statement: String, vararg params: Any?): QueryBuilder {
        rawQuery = Query(statement, params.toList())
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

    fun orderBy(vararg fields: OrderField): QueryBuilder {
        orderBy = OrderBy(fields.toList())
        return this
    }

    fun groupBy(vararg fields: String): QueryBuilder {
        groupBy = " GROUP BY ${fields.joinToString(", ") { it.wrap() }}"
        return this
    }

    fun having(condition: Condition): QueryBuilder {
        having = GenericHaving(condition)
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
        return rawQuery ?: Query("SELECT $fields${buildFrom()}$joins$where$groupBy$having$orderBy$limit", allParams())
    }

    private fun buildFrom(): From {
        return from ?: throw RuntimeException("Cannot generate query without table name")
    }

    private fun allParams() = where.params().plus(having.params())

    companion object {
        @JvmStatic
        fun raw(statement: String, vararg params: Any?) = QueryBuilder().raw(statement, *params).build()
    }
}
