package dbhelper.dsl

class RawStatement(private val statement: String, private val params: List<Any?> = emptyList()): Statement {
    override fun toString() = statement
    override fun params(): List<Any?> = params
}
