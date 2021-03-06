package jako.dsl.conditions

class And(private val left: Condition, private val right: Condition) : Condition {
    override fun toString() = "($left AND $right)"
    override fun params() = left.params().plus(right.params())
}

infix fun Condition.AND(value: Condition): And {
    return And(this, value)
}
