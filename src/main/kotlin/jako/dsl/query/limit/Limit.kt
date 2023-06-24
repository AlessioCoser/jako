package jako.dsl.query.limit

import jako.dsl.Dialect
import jako.dsl.StatementBlock

internal interface Limit: StatementBlock {
    override fun toSQL(dialect: Dialect): String
    override fun params(): List<Any?> = emptyList()
}