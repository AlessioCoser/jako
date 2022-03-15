package dbhelper.query

object Fields {
    fun String.wrap(): String {
        if(contains("(") and !contains("(\"")) {
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
