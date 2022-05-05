package jako.dsl.conditions

import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Eq(left: Field, right: Any?) : GenericCondition(left, "=", right) {
    constructor(left: String, right: Any?): this(Column(left), right)
}

infix fun String.EQ(value: String): Eq {
    return Eq(Column(this), value)
}

infix fun String.EQ(value: Int): Eq {
    return Eq(Column(this), value)
}

infix fun Field.EQ(value: String): Eq {
    return Eq(this, value)
}

infix fun Field.EQ(value: Int): Eq {
    return Eq(this, value)
}
