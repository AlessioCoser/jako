package dbhelper.query.conditions

class Eq(left: String, right: Any?) : GenericCondition(left, "=", right) {
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
