package jako.dsl.query.limit

import jako.dsl.Dialect

internal class NoLimit : Limit {
    override fun toSQL(dialect: Dialect) = ""
    override fun isPresent() = false
}
