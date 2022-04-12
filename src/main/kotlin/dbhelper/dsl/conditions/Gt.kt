package dbhelper.dsl.conditions

class Gt(left: String, right: Int) : GenericCondition(left, ">", right) {
    companion object {
        @JvmStatic
        infix fun String.GT(value: Int): Gt {
            return Gt(this, value)
        }
    }
}
