package jako.dsl.query.group

import jako.dsl.Dialect
import jako.dsl.StatementBlock

internal interface Group: StatementBlock {
    override fun toSQL(dialect: Dialect): String
    override fun params(): List<Any?> = emptyList()
}
