package dbhelper.dsl.conditions

infix fun String.eq(value: String): Condition {
    return Eq(this, value)
}

infix fun String.eq(value: Int): Condition {
    return Eq(this, value)
}

class Eq(private val left: String, private val right: Any?): Condition {
    override fun statement(): String {
        return "$left = ?"
    }

    override fun params() = listOf(right)
}