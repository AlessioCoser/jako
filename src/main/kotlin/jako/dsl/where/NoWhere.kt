package jako.dsl.where

import jako.dsl.Dialect

internal class NoWhere: Where {
    override fun toSQL(dialect: Dialect) = ""
    override fun params() = emptyList<String>()
    override fun isPresent() = false
}
