package dbhelper.dsl.fields

class Fields(private val fields: List<String>) {
    override fun toString() = fields.joinToString(separator = ", ") { it.wrap() }

    companion object {
        fun COUNT(fieldName: String) = "count(${fieldName.wrap()})"
        fun AVG(fieldName: String) = "avg(${fieldName.wrap()})"
        fun EVERY(fieldName: String) = "every(${fieldName.wrap()})"
        fun MAX(fieldName: String) = "max(${fieldName.wrap()})"
        fun MIN(fieldName: String) = "min(${fieldName.wrap()})"
        fun SUM(fieldName: String) = "sum(${fieldName.wrap()})"

        infix fun String.AS(name: String): String {
            return "${this.wrap()} AS ${name.wrap()}"
        }

        fun String.wrap(): String {
            if(contains("(*)") || this == "*" || contains("AS")) {
                return this
            }
            if(contains("(\"") || (startsWith("\"") && endsWith("\""))) {
                return this
            }
            if(contains("(")) {
                return this
                    .replace(".", "\".\"")
                    .replace("(", "(\"")
                    .replace(")", "\")")
            }
            return """"${this.replace(".", "\".\"")}""""
        }

        fun all(): Fields {
            return Fields(listOf("*"))
        }
    }
}
