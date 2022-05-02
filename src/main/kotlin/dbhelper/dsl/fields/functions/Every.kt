package dbhelper.dsl.fields.functions

import dbhelper.dsl.fields.Column
import dbhelper.dsl.fields.Field

class Every(private val value: Field): Field {
    override fun toString() = "EVERY($value)"

    companion object {
        @JvmStatic
        fun EVERY(fieldName: String) = Every(Column(fieldName))

        @JvmStatic
        fun EVERY(fieldName: Field) = Every(fieldName)
    }
}