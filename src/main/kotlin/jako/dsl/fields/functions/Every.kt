package jako.dsl.fields.functions

import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Every(private val value: Field): Field {
    override fun toString() = "EVERY($value)"
    override fun params() = value.params()

    companion object {
        @JvmStatic
        fun EVERY(fieldName: String) = Every(Column(fieldName))

        @JvmStatic
        fun EVERY(fieldName: Field) = Every(fieldName)
    }
}