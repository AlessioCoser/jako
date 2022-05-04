package jako.dsl.query.join

import jako.dsl.StatementBlock

internal interface Join: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?> = emptyList()
}