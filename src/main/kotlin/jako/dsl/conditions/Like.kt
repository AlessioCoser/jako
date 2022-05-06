package jako.dsl.conditions

import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Like(left: Field, right: String) : GenericCondition(left, "LIKE", right) {
    constructor(left: String, right: String): this(Column(left), right)
}

infix fun String.LIKE(value: String): Like {
    return Like(this, value)
}

infix fun String.STARTS_WITH(value: String): Like {
    return Like(this, "$value%")
}

infix fun String.ENDS_WITH(value: String): Like {
    return Like(this, "%$value")
}

infix fun String.CONTAINS(value: String): Like {
    return Like(this, "%$value%")
}
