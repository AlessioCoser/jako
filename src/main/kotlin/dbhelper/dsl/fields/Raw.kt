package dbhelper.dsl.fields

class Raw(private val value: Any): Field {
    override fun toString() = value.toString()
    override fun params(): List<Any?> = emptyList()

    companion object {
        @JvmStatic
        val String.raw: Field
            get() = Raw(this)
    }
}