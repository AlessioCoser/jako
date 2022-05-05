package jako.dsl.conditions

class Or(private val left: Condition, private val right: Condition) : Condition {
    override fun toString() = "($left OR $right)"
    override fun params() = left.params().plus(right.params())
}

infix fun Condition.OR(value: Condition): Or {
    return Or(this, value)
}
