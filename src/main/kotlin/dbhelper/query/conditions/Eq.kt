package dbhelper.query.conditions

class Eq(left: String, right: Any?) : GenericCondition(left, "=", right) {
    companion object {
        @JvmStatic
        infix fun String.eq(value: String): Eq {
            return Eq(this, value)
        }

        @JvmStatic
        infix fun String.eq(value: Int): Eq {
            return Eq(this, value)
        }
    }
}
