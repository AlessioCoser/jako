package jako.dsl.fields.functions

import jako.dsl.Dialect
import jako.dsl.fields.Column
import jako.dsl.fields.Field
import jako.dsl.fields.Value

class Coalesce(private val value: Field, private val default: Field): Field {
    override fun toSQL(dialect: Dialect) = "COALESCE(${value.toSQL(dialect)}, ${default.toSQL(dialect)})"
    override fun params() = value.params() + default.params()
}

fun COALESCE(fieldName: String, default: Any?) = Coalesce(Column(fieldName), Value(default))

fun COALESCE(fieldName: Field, default: Any?) = Coalesce(fieldName, Value(default))
