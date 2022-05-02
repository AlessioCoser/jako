package dbhelper.dsl.fields.functions

import dbhelper.dsl.fields.Column
import dbhelper.dsl.fields.Field

class Sum(private val value: Field): Field {
    override fun toString() = "SUM($value)"
    override fun params() = value.params()

    companion object {
        @JvmStatic
        fun SUM(fieldName: String) = Sum(Column(fieldName))

        @JvmStatic
        fun SUM(fieldName: Field) = Sum(fieldName)
    }
}