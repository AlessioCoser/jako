package dbhelper.dsl.fields.functions

import dbhelper.dsl.fields.Column
import dbhelper.dsl.fields.Field

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