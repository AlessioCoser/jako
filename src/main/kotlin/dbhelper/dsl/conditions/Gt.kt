package dbhelper.dsl.conditions

import dbhelper.dsl.fields.Column
import dbhelper.dsl.fields.Field

class Gt(left: Field, right: Int) : GenericCondition(left, ">", right) {
    constructor(left: String, right: Int): this(Column(left), right)

    companion object {
        @JvmStatic
        infix fun String.GT(value: Int): Gt {
            return Gt(Column(this), value)
        }
    }
}
