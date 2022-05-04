package jako.dsl.query.group

import jako.dsl.StatementBlock

internal interface Group: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?> = emptyList()
}
