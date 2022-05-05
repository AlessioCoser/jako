package jako.dsl.fields.functions

import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Avg(private val value: Field): Field {
    override fun toString() = "AVG($value)"
    override fun params() = value.params()
}

fun AVG(fieldName: String) = Avg(Column(fieldName))

fun AVG(field: Field) = Avg(field)
