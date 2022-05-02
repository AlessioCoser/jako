package dbhelper.dsl.conditions

import dbhelper.dsl.fields.Column
import dbhelper.dsl.fields.Field

class IsNull(private val column: Field): Condition {
    constructor(column: String): this(Column(column))

    override fun toString() = "$column IS NULL"

    override fun params() = emptyList<Any?>()

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
