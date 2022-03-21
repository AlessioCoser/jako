package dbhelper.query

import dbhelper.Statement
import dbhelper.query.fields.Fields
import dbhelper.query.group.Group
import dbhelper.query.having.Having
import dbhelper.query.join.Joins
import dbhelper.query.limit.Limit
import dbhelper.query.order.Order
import dbhelper.query.where.Where

data class Query(override val statement: String, override val params: List<Any?>): Statement {
    constructor(
        fields: Fields,
        from: From,
        joins: Joins,
        where: Where,
        groupBy: Group,
        having: Having,
        orderBy: Order,
        limit: Limit
    ) : this("SELECT $fields$from$joins$where$groupBy$having$orderBy$limit", where.params().plus(having.params()))
}
