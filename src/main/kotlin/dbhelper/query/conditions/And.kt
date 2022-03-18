package dbhelper.query.conditions

class And(private val left: Condition, private val right: Condition) : Condition {
    override fun toString(): String {
        return "($left AND $right)"
    }

    override fun params() = left.params().plus(right.params())

    companion object {
        @JvmStatic
        infix fun Condition.AND(value: Condition): And {
            return And(this, value)
        }
    }
}
