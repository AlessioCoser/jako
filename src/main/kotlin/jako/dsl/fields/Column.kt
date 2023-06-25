package jako.dsl.fields

import jako.dsl.Dialect

class Column(private val value: String): Field {
    override fun toSQL(dialect: Dialect) = wrap(dialect, value)
    override fun params(): List<Any?> = emptyList()
    override fun isPresent() = value.isNotBlank()

    private fun wrap(dialect: Dialect, value: String): String {
        val sep = dialect.columnSeparator

        if(value.contains("(*)") || value == "*" || value.contains("AS")) {
            return value
        }
        if(value.contains("($sep") || (value.startsWith(sep) && value.endsWith(sep))) {
            return value
        }
        if(value.contains("(")) {
            return value
                .replace(".", "$sep.$sep")
                .replace("(", "($sep")
                .replace(")", "$sep)")
        }
        return """$sep${value.replace(".", "$sep.$sep")}$sep"""
    }
}

val String.col: Field
    get() = Column(this)
