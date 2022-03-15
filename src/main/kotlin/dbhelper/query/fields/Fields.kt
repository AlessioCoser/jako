package dbhelper.query.fields

object Fields {
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
