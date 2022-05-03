package dbhelper.dsl.query.order

import dbhelper.dsl.StatementBlock

interface Order: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?> = emptyList()
}

