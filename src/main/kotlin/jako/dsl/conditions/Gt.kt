package jako.dsl.conditions

import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Gt(left: Field, right: Int) : GenericCondition(left, ">", right) {
    constructor(left: String, right: Int): this(Column(left), right)

    companion object {
        @JvmStatic
        infix fun String.GT(value: Int): Gt {
            return Gt(Column(this), value)
        }

        @JvmStatic
        infix fun Field.GT(value: Int): Gt {
            return Gt(this, value)
        }
    }
}
