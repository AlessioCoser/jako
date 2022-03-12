package dbhelper.query.conditions

class Or(private val left: Condition, private val right: Condition) : Condition {
    override fun statement(): String {
        return "(${left.statement()} OR ${right.statement()})"
    }

    override fun params() = left.params().plus(right.params())

    companion object {
        @JvmStatic
        infix fun Condition.or(value: Condition): Or {
            return Or(this, value)
        }
    }
}
