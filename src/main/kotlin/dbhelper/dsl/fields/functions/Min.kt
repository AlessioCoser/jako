package dbhelper.dsl.fields.functions

import dbhelper.dsl.fields.Column
import dbhelper.dsl.fields.Field

class Min(private val value: Field): Field {
    override fun toString() = "MIN($value)"

    companion object {
        @JvmStatic
        fun MIN(fieldName: String) = Min(Column(fieldName))

        @JvmStatic
        fun MIN(fieldName: Field) = Min(fieldName)
    }
}