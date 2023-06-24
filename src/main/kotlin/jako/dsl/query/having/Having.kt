package jako.dsl.query.having

import jako.dsl.Dialect
import jako.dsl.StatementBlock

internal interface Having: StatementBlock {
    override fun toSQL(dialect: Dialect): String
    override fun params(): List<Any?>
}
