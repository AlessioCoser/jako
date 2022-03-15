package dbhelper.query.conditions

class Gt(left: String, right: Any?) : GenericCondition(left, ">", right) {
    companion object {
        @JvmStatic
        infix fun String.GT(value: Int): Gt {
            return Gt(this, value)
        }
    }
}
