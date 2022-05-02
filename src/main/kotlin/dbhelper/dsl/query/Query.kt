package dbhelper.dsl.query

import dbhelper.dsl.Statement
import dbhelper.dsl.fields.Field
import dbhelper.dsl.query.group.Group
import dbhelper.dsl.query.having.Having
import dbhelper.dsl.query.join.Joins
import dbhelper.dsl.query.limit.Limit
import dbhelper.dsl.query.order.Order
import dbhelper.dsl.where.Where

data class Query(override val statement: String, override val params: List<Any?>): Statement {
    constructor(
        fields: Field,
        from: From,
        joins: Joins,
        where: Where,
        groupBy: Group,
        having: Having,
        orderBy: Order,
        limit: Limit
    ) : this("SELECT $fields$from$joins$where$groupBy$having$orderBy$limit", fields.params() + where.params() + having.params())
}
