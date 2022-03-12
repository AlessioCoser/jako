package dbhelper.dsl.conditions

class Eq(private val left: String, private val right: Any?) : Condition {
    override fun statement(): String {
        return "$left = ?"
    }

    override fun params() = listOf(right)

    companion object {
        @JvmStatic
        infix fun String.eq(value: String): Condition {
            return Eq(this, value)
        }

        @JvmStatic
        infix fun String.eq(value: Int): Condition {
            return Eq(this, value)
        }
    }
}
