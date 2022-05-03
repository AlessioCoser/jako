package jako.dsl.fields.functions

import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Avg(private val value: Field): Field {
    override fun toString() = "AVG($value)"
    override fun params() = value.params()

    companion object {
        @JvmStatic
        fun AVG(fieldName: String) = Avg(Column(fieldName))

        @JvmStatic
        fun AVG(field: Field) = Avg(field)
    }
}