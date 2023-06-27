package jako.dsl.conditions

import jako.dsl.Dialect

class Or(private val left: Condition, private val right: Condition) : Condition {
    override fun toSQL(dialect: Dialect) = "(${presentFields().joinToString(" OR ") { it.toSQL(dialect) }})"
    override fun params() = presentFields().flatMap { it.params() }
    override fun isPresent() = left.isPresent() || right.isPresent()

    private fun presentFields() = listOf(left, right).filter { it.isPresent() }
}

infix fun Condition.OR(value: Condition): Or {
    return Or(this, value)
}
