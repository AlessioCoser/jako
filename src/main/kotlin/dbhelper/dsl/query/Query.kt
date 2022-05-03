package dbhelper.dsl.query

import dbhelper.dsl.Statement
import dbhelper.dsl.conditions.Condition
import dbhelper.dsl.fields.Field
import dbhelper.dsl.fields.Field.Companion.ALL
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

class Query: Statement {
    private var from: From? = null
    private var fields: Field = ALL
    private var where: Where = NoWhere()
    private var joins: Joins = Joins()
    private var having: Having = NoHaving()
    private var orderBy: Order = NoOrder()
    private var groupBy: Group = NoGroup()
    private var limit: Limit = NoLimit()

    override fun toString() = "SELECT $fields${fromOrThrow()}$joins$where$groupBy$having$orderBy$limit"
    override fun params(): List<Any?> = fields.params() + where.params() + having.params()

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
        groupBy = GroupBy(fields.toList())
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
}
