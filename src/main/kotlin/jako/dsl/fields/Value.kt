package jako.dsl.fields

import jako.dsl.Dialect


class Value(private val value: Any?): Field {
    override fun toSQL(dialect: Dialect) = "?"
    override fun params() = listOf(value)
    override fun isPresent() = true
}

val Any?.value: Value
    get() = Value(this)
