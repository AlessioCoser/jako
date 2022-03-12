package dbhelper.dsl.query.conditions

class Gt(left: String, right: Any?) : GenericCondition(left, ">", right) {
    companion object {
        @JvmStatic
        infix fun String.gt(value: Int): Condition {
            return Gt(this, value)
        }
    }
}
