package dbhelper.dsl.fields.functions

import dbhelper.dsl.fields.Column
import dbhelper.dsl.fields.Field
import dbhelper.dsl.fields.Value

class Coalesce(private val value: Field, private val default: Field): Field {
    override fun toString() = "COALESCE($value, $default)"
    override fun params() = value.params() + default.params()

    companion object {
        @JvmStatic
        fun COALESCE(fieldName: String, default: Any?) = Coalesce(Column(fieldName), Value(default))

        @JvmStatic
        fun COALESCE(fieldName: Field, default: Any?) = Coalesce(fieldName, Value(default))
    }
}