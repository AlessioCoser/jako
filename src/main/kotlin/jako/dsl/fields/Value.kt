package jako.dsl.fields


class Value(private val value: Any?): Field {
    override fun toString() = "?"
    override fun params() = listOf(value)
}

val Any?.value: Value
    get() = Value(this)
