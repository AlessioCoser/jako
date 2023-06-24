package jako.dsl.conditions

import jako.dsl.Dialect

class And(private val left: Condition, private val right: Condition) : Condition {
    override fun toSQL(dialect: Dialect) = "(${left.toSQL(dialect)} AND ${right.toSQL(dialect)})"
    override fun params() = left.params().plus(right.params())
}

infix fun Condition.AND(value: Condition): And {
    return And(this, value)
}
