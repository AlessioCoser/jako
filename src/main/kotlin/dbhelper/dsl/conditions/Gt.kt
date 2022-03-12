package dbhelper.dsl.conditions

class Gt(private val left: String, private val right: Any?) : Condition {
    override fun statement(): String {
        return "$left > ?"
    }

    override fun params() = listOf(right)

    companion object {
        @JvmStatic
        infix fun String.gt(value: Int): Condition {
            return Gt(this, value)
        }
    }
}
