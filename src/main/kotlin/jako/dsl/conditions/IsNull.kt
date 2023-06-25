package jako.dsl.conditions

import jako.dsl.Dialect
import jako.dsl.fields.Column
import jako.dsl.fields.Field

class IsNull(private val column: Field): Condition {
    constructor(column: String): this(Column(column))

    override fun toSQL(dialect: Dialect) = "${column.toSQL(dialect)} IS NULL"
    override fun params() = column.params()
    override fun isPresent() = column.isPresent()
}

infix fun String.IS(nothing: Nothing?): Condition {
    return IsNull(this)
}

infix fun Field.IS(nothing: Nothing?): Condition {
    return IsNull(this)
}
