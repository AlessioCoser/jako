package dbhelper.dsl.conditions

infix fun Condition.and(value: Condition): Condition {
    return And(this, value)
}

class And(private val left: Condition, private val right: Condition): Condition {
    override fun statement(): String {
        return "(${left.statement()} AND ${right.statement()})"
    }

    override fun params() = left.params().plus(right.params())
}