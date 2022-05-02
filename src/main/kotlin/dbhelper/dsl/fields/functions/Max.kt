package dbhelper.dsl.fields.functions

import dbhelper.dsl.fields.Column
import dbhelper.dsl.fields.Field

class Max(private val value: Field): Field {
    override fun toString() = "MAX($value)"
    override fun params() = value.params()

    companion object {
        @JvmStatic
        fun MAX(fieldName: String) = Max(Column(fieldName))

        @JvmStatic
        fun MAX(field: Field) = Max(field)
    }
}