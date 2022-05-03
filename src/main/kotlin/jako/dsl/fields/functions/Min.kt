package jako.dsl.fields.functions

import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Min(private val value: Field): Field {
    override fun toString() = "MIN($value)"
    override fun params() = value.params()

    companion object {
        @JvmStatic
        fun MIN(fieldName: String) = Min(Column(fieldName))

        @JvmStatic
        fun MIN(fieldName: Field) = Min(fieldName)
    }
}