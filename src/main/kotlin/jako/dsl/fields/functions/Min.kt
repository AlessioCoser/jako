package jako.dsl.fields.functions

import jako.dsl.fields.Column
import jako.dsl.fields.Field

class Min(private val value: Field): Field {
    override fun toString() = "MIN($value)"
    override fun params() = value.params()
}

fun MIN(fieldName: String) = Min(Column(fieldName))

fun MIN(fieldName: Field) = Min(fieldName)
