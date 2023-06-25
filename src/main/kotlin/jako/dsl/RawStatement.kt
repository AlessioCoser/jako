package jako.dsl

class RawStatement(private val statement: String, private val params: List<Any?> = emptyList()): Statement {
    override fun toSQL(dialect: Dialect) = statement
    override fun params(): List<Any?> = params
    override fun isPresent() = statement.isNotBlank()
}
