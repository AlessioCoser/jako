package jako.dsl.conditions

import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Gt(left: Field, right: Int) : GenericCondition(left, ">", right) {
    constructor(left: String, right: Int): this(Column(left), right)
}

infix fun String.GT(value: Int): Gt {
    return Gt(Column(this), value)
}

infix fun Field.GT(value: Int): Gt {
    return Gt(this, value)
}
