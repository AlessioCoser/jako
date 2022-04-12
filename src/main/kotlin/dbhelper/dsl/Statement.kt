package dbhelper.dsl

interface Statement {
    val statement: String
    val params: List<Any?>
}
