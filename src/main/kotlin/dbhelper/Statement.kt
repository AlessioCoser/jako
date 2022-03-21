package dbhelper

interface Statement {
    val statement: String
    val params: List<Any?>
}