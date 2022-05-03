package dbhelper.dsl.query.having

import dbhelper.dsl.StatementBlock

interface Having: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?>
}
