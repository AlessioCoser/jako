package dbhelper.dsl.fields.functions

import dbhelper.dsl.fields.Column
import dbhelper.dsl.fields.Field

class Avg(private val value: Field): Field {
    override fun toString() = "AVG($value)"

    companion object {
        @JvmStatic
        fun AVG(fieldName: String) = Avg(Column(fieldName))

        @JvmStatic
        fun AVG(field: Field) = Avg(field)
    }
}