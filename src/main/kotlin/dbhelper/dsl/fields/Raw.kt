package dbhelper.dsl.fields

class Raw(private val value: Any): Field {
    override fun toString() = value.toString()

    companion object {
        @JvmStatic
        val String.raw: Field
            get() = Raw(this)
    }
}