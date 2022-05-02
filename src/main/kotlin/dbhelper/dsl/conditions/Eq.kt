package dbhelper.dsl.conditions

import dbhelper.dsl.fields.Column
import dbhelper.dsl.fields.Field

class Eq(left: Field, right: Any?) : GenericCondition(left, "=", right) {
    constructor(left: String, right: Any?): this(Column(left), right)

    companion object {
        @JvmStatic
        infix fun String.EQ(value: String): Eq {
            return Eq(Column(this), value)
        }

        @JvmStatic
        infix fun String.EQ(value: Int): Eq {
            return Eq(Column(this), value)
        }

        @JvmStatic
        infix fun Field.EQ(value: String): Eq {
            return Eq(this, value)
        }

        @JvmStatic
        infix fun Field.EQ(value: Int): Eq {
            return Eq(this, value)
        }
    }
}
