package jako.dsl.conditions

import jako.dsl.fields.Column
import jako.dsl.fields.Field

class NotNull(private val column: Field): Condition {
    constructor(column: String): this(Column(column))

    override fun toString() = "$column IS NOT NULL"
    override fun params() = column.params()

    companion object {
        @JvmStatic
        infix fun String.NOT(nothing: Nothing?): Condition {
            return NotNull(this)
        }

        @JvmStatic
        infix fun Field.NOT(nothing: Nothing?): Condition {
            return NotNull(this)
        }
    }
}