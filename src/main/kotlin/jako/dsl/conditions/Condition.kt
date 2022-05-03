package jako.dsl.conditions

import jako.dsl.StatementBlock

interface Condition: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?>
}
