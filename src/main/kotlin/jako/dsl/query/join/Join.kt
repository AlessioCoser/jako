package jako.dsl.query.join

import jako.dsl.StatementBlock

interface Join: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?> = emptyList()
}