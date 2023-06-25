package jako.dsl

interface StatementBlock {
    fun toSQL(dialect: Dialect): String
    fun params(): List<Any?>
    fun isPresent(): Boolean
}
