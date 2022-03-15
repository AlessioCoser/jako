package dbhelper.query.fields

class Fields(private val fields: Array<out String>) {
    fun statement() = fields.joinToString(separator = ", ") { it.wrap() }

    companion object {
        fun COUNT(fieldName: String) = "count(${fieldName.wrap()})"
        fun AVG(fieldName: String) = "avg(${fieldName.wrap()})"
        fun EVERY(fieldName: String) = "every(${fieldName.wrap()})"
        fun MAX(fieldName: String) = "max(${fieldName.wrap()})"
        fun MIN(fieldName: String) = "min(${fieldName.wrap()})"
        fun SUM(fieldName: String) = "sum(${fieldName.wrap()})"

        fun String.wrap(): String {
            if(contains("(*)") || this == "*") {
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

        infix fun String.AS(name: String): String {
            return "$this AS ${name.wrap()}"
        }
    }
}
