package jako.dsl.fields.functions

import jako.dsl.Dialect
import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Avg(private val value: Field): Field {
    override fun toSQL(dialect: Dialect) = "AVG(${value.toSQL(dialect)})"
    override fun params() = value.params()
    override fun isPresent() = value.isPresent()
}

fun AVG(fieldName: String) = Avg(Column(fieldName))

fun AVG(field: Field) = Avg(field)
