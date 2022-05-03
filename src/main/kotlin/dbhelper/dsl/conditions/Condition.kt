package dbhelper.dsl.conditions

import dbhelper.dsl.StatementBlock

interface Condition: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?>
}
