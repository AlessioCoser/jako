package dbhelper.dsl.fields

class Raw(private val value: Any, private val params: List<Any?> = emptyList()): Field {
    override fun toString() = value.toString()
    override fun params(): List<Any?> = params

    companion object {
        @JvmStatic
        val String.raw: Field
            get() = Raw(this)
    }
}