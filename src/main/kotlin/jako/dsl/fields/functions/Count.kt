package jako.dsl.fields.functions

import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Count(private val value: Field): Field {
    override fun toString() = "COUNT($value)"
    override fun params() = value.params()

    companion object {
        @JvmStatic
        fun COUNT(fieldName: String) = Count(Column(fieldName))

        @JvmStatic
        fun COUNT(field: Field) = Count(field)
    }
}