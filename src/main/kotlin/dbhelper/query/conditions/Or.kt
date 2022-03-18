package dbhelper.query.conditions

class Or(private val left: Condition, private val right: Condition) : Condition {
    override fun toString(): String {
        return "($left OR $right)"
    }

    override fun params() = left.params().plus(right.params())

    companion object {
        @JvmStatic
        infix fun Condition.OR(value: Condition): Or {
            return Or(this, value)
        }
    }
}
