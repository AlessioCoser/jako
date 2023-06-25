package jako.dsl

abstract class StatementBuilder internal constructor(): Statement {
    private val blocks: List<StatementBlock> by lazy { blocks() }

    override fun toSQL(dialect: Dialect) = blocks.filter { it.isPresent() }.joinToString(" ") { it.toSQL(dialect) }

    override fun params(): List<Any?> = blocks.flatMap { it.params() }

    protected abstract fun blocks(): List<StatementBlock>

    override fun isPresent() = blocks.any { it.isPresent() }
}
