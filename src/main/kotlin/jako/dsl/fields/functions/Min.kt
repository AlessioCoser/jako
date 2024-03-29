package jako.dsl.fields.functions

import jako.dsl.Dialect
import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Min(private val value: Field): Field {
    override fun toSQL(dialect: Dialect) = "MIN(${value.toSQL(dialect)})"
    override fun params() = value.params()
    override fun isPresent() = value.isPresent()
}

fun MIN(fieldName: String) = Min(Column(fieldName))

fun MIN(fieldName: Field) = Min(fieldName)
