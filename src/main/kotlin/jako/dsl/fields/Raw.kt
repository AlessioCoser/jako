package jako.dsl.fields

import jako.dsl.Dialect

class Raw(private val value: Any, private val params: List<Any?> = emptyList()): Field {
    constructor(value: Field): this(value, value.params())

    override fun toSQL(dialect: Dialect) = if (value is Field) value.toSQL(dialect) else value.toString()
    override fun params(): List<Any?> = params
    override fun isPresent() = value.toString().isNotBlank()
}

val String.raw: Field
    get() = Raw(this)
