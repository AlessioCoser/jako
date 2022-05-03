package jako.dsl.query.group

import jako.dsl.StatementBlock

interface Group: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?> = emptyList()
}
