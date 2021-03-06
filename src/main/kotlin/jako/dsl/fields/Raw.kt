package jako.dsl.fields

class Raw(private val value: Any, private val params: List<Any?> = emptyList()): Field {
    constructor(value: Field): this(value.toString(), value.params())

    override fun toString() = value.toString()
    override fun params(): List<Any?> = params
}

val String.raw: Field
    get() = Raw(this)
