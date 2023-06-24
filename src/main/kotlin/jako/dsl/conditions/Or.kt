package jako.dsl.conditions

import jako.dsl.Dialect

class Or(private val left: Condition, private val right: Condition) : Condition {
    override fun toSQL(dialect: Dialect) = "(${left.toSQL(dialect)} OR ${right.toSQL(dialect)})"
    override fun params() = left.params().plus(right.params())
}

infix fun Condition.OR(value: Condition): Or {
    return Or(this, value)
}
