package dbhelper.dsl.fields

class Column(private val value: String): Field {
    override fun toString() = wrap(value)
    override fun params(): List<Any?> = emptyList()

    private fun wrap(value: String): String {
        if(value.contains("(*)") || value == "*" || value.contains("AS")) {
            return value
        }
        if(value.contains("(\"") || (value.startsWith("\"") && value.endsWith("\""))) {
            return value
        }
        if(value.contains("(")) {
            return value
                .replace(".", "\".\"")
                .replace("(", "(\"")
                .replace(")", "\")")
        }
        return """"${value.replace(".", "\".\"")}""""
    }

    companion object {
        @JvmStatic
        val String.col: Field
            get() = Column(this)
    }
}