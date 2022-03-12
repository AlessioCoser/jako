package dbhelper.query.conditions

class Gt(left: String, right: Any?) : GenericCondition(left, ">", right) {
    companion object {
        @JvmStatic
        infix fun String.gt(value: Int): Gt {
            return Gt(this, value)
        }
    }
}
