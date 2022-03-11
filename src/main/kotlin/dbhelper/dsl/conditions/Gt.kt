package dbhelper.dsl.conditions

infix fun String.gt(value: Int): Condition {
    return Gt(this, value)
}

class Gt(private val left: String, private val right: Any?): Condition {
    override fun statement(): String {
        return "$left > ?"
    }

    override fun params() = listOf(right)
}