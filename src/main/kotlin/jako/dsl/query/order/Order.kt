package jako.dsl.query.order

import jako.dsl.StatementBlock

interface Order: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?> = emptyList()
}

