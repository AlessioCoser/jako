package jako.dsl.query.limit

import jako.dsl.StatementBlock

interface Limit: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?> = emptyList()
}