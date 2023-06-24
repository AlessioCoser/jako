package jako.dsl.query.limit

import jako.dsl.Dialect

internal class LimitTo(private val limit: Int, private val offset: Int = 0) : Limit {
    override fun toSQL(dialect: Dialect) = if (offset != 0) {
        "LIMIT $limit OFFSET $offset"
    } else {
        "LIMIT $limit"
    }
}
