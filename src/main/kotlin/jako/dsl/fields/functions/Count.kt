package jako.dsl.fields.functions

import jako.dsl.Dialect
import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Count(private val value: Field): Field {
    override fun toSQL(dialect: Dialect) = "COUNT(${value.toSQL(dialect)})"
    override fun params() = value.params()
    override fun isPresent() = value.isPresent()
}

fun COUNT(fieldName: String) = Count(Column(fieldName))

fun COUNT(field: Field) = Count(field)
