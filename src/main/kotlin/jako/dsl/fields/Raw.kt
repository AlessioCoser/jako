package jako.dsl.fields

import jako.dsl.Dialect

class Raw(private val value: Any, private val params: List<Any?> = emptyList()): Field {
    constructor(value: Field): this(value.toSQL(), value.params())

    override fun toSQL(dialect: Dialect) = value.toString()
    override fun params(): List<Any?> = params
}

val String.raw: Field
    get() = Raw(this)
