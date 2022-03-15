package dbhelper.query

object Wrap {
    fun String.wrap(): String {
//        if(contains("(") and !contains("(\"")) {
//            return this
//                .replace(".", "\".\"")
//                .replace("(", "(\"")
//                .replace(")", "\")")
//        }
        return """"${this.replace(".", "\".\"")}""""
    }
}
