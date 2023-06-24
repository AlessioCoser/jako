package jako.dsl.fields.functions

import jako.dsl.Dialect
import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Max(private val value: Field): Field {
    override fun toSQL(dialect: Dialect) = "MAX(${value.toSQL(dialect)})"
    override fun params() = value.params()
}

fun MAX(fieldName: String) = Max(Column(fieldName))

fun MAX(field: Field) = Max(field)
