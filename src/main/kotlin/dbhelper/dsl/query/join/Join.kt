package dbhelper.dsl.query.join

import dbhelper.dsl.StatementBlock

interface Join: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?> = emptyList()
}