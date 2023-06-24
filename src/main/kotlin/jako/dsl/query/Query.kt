package jako.dsl.query

import jako.dsl.StatementBuilder
import jako.dsl.conditions.Condition
import jako.dsl.fields.ALL
import jako.dsl.fields.Field
import jako.dsl.fields.Fields
import jako.dsl.fields.Raw
import jako.dsl.query.group.Group
import jako.dsl.query.group.GroupBy
import jako.dsl.query.group.NoGroup
import jako.dsl.query.having.GenericHaving
import jako.dsl.query.having.Having
import jako.dsl.query.having.NoHaving
import jako.dsl.query.join.Joins
import jako.dsl.query.join.On
import jako.dsl.query.limit.Limit
import jako.dsl.query.limit.LimitTo
import jako.dsl.query.limit.NoLimit
import jako.dsl.query.order.NoOrder
import jako.dsl.query.order.Order
import jako.dsl.query.order.OrderBy
import jako.dsl.query.order.OrderField
import jako.dsl.where.GenericWhere
import jako.dsl.where.NoWhere
import jako.dsl.where.Where

class Query: StatementBuilder() {
    private var from: From? = null
    private var fields: Field = ALL
    private var where: Where = NoWhere()
    private var joins: Joins = Joins()
    private var having: Having = NoHaving()
    private var orderBy: Order = NoOrder()
    private var groupBy: Group = NoGroup()
    private var limit: Limit = NoLimit()

    override fun blocks() = listOf(Raw("SELECT"), fields, fromOrThrow(), joins, where, groupBy, having, orderBy, limit)

    fun from(table: String): Query {
        this.from = From(table)
        return this
    }

    fun fields(vararg fields: String): Query {
        this.fields = Fields(*fields)
        return this
    }

    fun fields(vararg fields: Field): Query {
        this.fields = Fields(*fields)
        return this
    }

    fun where(condition: Condition): Query {
        where = GenericWhere(condition)
        return this
    }

    fun join(on: On): Query {
        joins.join(on)
        return this
    }
    
    fun leftJoin(on: On): Query {
        joins.leftJoin(on)
        return this
    }

    fun rightJoin(on: On): Query {
        joins.rightJoin(on)
        return this
    }

    fun orderBy(vararg fields: OrderField): Query {
        orderBy = OrderBy(fields.toList())
        return this
    }

    fun groupBy(vararg fields: String): Query {
        groupBy = GroupBy(*fields)
        return this
    }

    fun having(condition: Condition): Query {
        having = GenericHaving(condition)
        return this
    }

    fun limit(limit: Int, offset: Int = 0): Query {
        this.limit = LimitTo(limit, offset)
        return this
    }

    fun single() = limit(1)

    private fun fromOrThrow(): From = from ?: throw RuntimeException("Cannot generate query without table name")

    companion object {
        fun from(table: String): Query {
            return Query().from(table)
        }
    }
}
