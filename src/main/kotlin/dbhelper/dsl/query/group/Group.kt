package dbhelper.dsl.query.group

import dbhelper.dsl.StatementBlock

interface Group: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?> = emptyList()
}
