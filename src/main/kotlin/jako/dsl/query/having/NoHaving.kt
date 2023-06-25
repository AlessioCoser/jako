package jako.dsl.query.having

import jako.dsl.Dialect

internal class NoHaving: Having {
    override fun toSQL(dialect: Dialect) = ""
    override fun params() = emptyList<String>()
    override fun isPresent() = false
}
