package dbhelper.dsl.query.limit

import dbhelper.dsl.StatementBlock

interface Limit: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?> = emptyList()
}