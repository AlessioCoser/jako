package jako.dsl.conditions

import jako.dsl.fields.Column
import jako.dsl.fields.Field

class IsNull(private val column: Field): Condition {
    constructor(column: String): this(Column(column))

    override fun toString() = "$column IS NULL"
    override fun params() = column.params()

    companion object {
        @JvmStatic
        infix fun String.IS(nothing: Nothing?): Condition {
            return IsNull(this)
        }

        @JvmStatic
        infix fun Field.IS(nothing: Nothing?): Condition {
            return IsNull(this)
        }
    }
}
