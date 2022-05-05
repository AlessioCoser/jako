package jako.dsl.fields.functions

import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Sum(private val value: Field): Field {
    override fun toString() = "SUM($value)"
    override fun params() = value.params()
}

fun SUM(fieldName: String) = Sum(Column(fieldName))

fun SUM(fieldName: Field) = Sum(fieldName)
