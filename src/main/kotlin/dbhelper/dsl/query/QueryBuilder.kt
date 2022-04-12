package dbhelper.dsl.query

import dbhelper.dsl.conditions.Condition
import dbhelper.dsl.fields.Fields
import dbhelper.dsl.query.group.Group
import dbhelper.dsl.query.group.GroupBy
import dbhelper.dsl.query.group.NoGroup
import dbhelper.dsl.query.having.GenericHaving
import dbhelper.dsl.query.having.Having
import dbhelper.dsl.query.having.NoHaving
import dbhelper.dsl.query.join.Joins
import dbhelper.dsl.query.join.On
import dbhelper.dsl.query.limit.Limit
import dbhelper.dsl.query.limit.LimitTo
import dbhelper.dsl.query.limit.NoLimit
import dbhelper.dsl.query.order.NoOrder
import dbhelper.dsl.query.order.Order
import dbhelper.dsl.query.order.OrderBy
import dbhelper.dsl.query.order.OrderField
import dbhelper.dsl.where.GenericWhere
import dbhelper.dsl.where.NoWhere
import dbhelper.dsl.where.Where

class QueryBuilder {
    private var rawQuery: Query? = null
    private var from: From? = null
    private var fields: Fields = Fields.all()
    private var where: Where = NoWhere()
    private var joins: Joins = Joins()
    private var having: Having = NoHaving()
    private var orderBy: Order = NoOrder()
    private var groupBy: Group = NoGroup()
    private var limit: Limit = NoLimit()

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
        groupBy = GroupBy(fields.toList())
        return this
    }

    fun having(condition: Condition): QueryBuilder {
        having = GenericHaving(condition)
        return this
    }

    fun limit(limit: Int, offset: Int = 0): QueryBuilder {
        this.limit = LimitTo(limit, offset)
        return this
    }

    fun single() = limit(1)

    fun build(): Query {
        return rawQuery ?: Query(fields, fromOrThrow(), joins, where, groupBy, having, orderBy, limit)
    }

    private fun fromOrThrow(): From = from ?: throw RuntimeException("Cannot generate query without table name")

    companion object {
        @JvmStatic
        fun raw(statement: String, vararg params: Any?) = QueryBuilder().raw(statement, *params).build()
    }
}
