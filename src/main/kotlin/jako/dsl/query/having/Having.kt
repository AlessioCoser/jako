package jako.dsl.query.having

import jako.dsl.StatementBlock

internal interface Having: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?>
}
