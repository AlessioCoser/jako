package jako.dsl.fields.functions

import jako.dsl.Dialect
import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Every(private val value: Field): Field {
    override fun toSQL(dialect: Dialect) = "EVERY(${value.toSQL(dialect)})"
    override fun params() = value.params()
    override fun isPresent() = value.isPresent()
}

fun EVERY(fieldName: String) = Every(Column(fieldName))

fun EVERY(fieldName: Field) = Every(fieldName)
