package jako.dsl.fields.functions

import jako.dsl.Dialect
import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Sum(private val value: Field): Field {
    override fun toSQL(dialect: Dialect) = "SUM(${value.toSQL(dialect)})"
    override fun params() = value.params()
    override fun isPresent() = value.isPresent()
}

fun SUM(fieldName: String) = Sum(Column(fieldName))

fun SUM(fieldName: Field) = Sum(fieldName)
