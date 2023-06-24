package jako.dsl.conditions

import jako.dsl.Dialect
import jako.dsl.StatementBlock

interface Condition: StatementBlock {
    override fun toSQL(dialect: Dialect): String
    override fun params(): List<Any?>
}
