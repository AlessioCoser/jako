package jako.dsl

abstract class StatementBuilder internal constructor(): Statement {
    private val blocks: List<StatementBlock> by lazy { blocks() }

    override fun toSQL(dialect: Dialect) = presentBlocks().joinToString(" ") { it.toSQL(dialect) }

    override fun params(): List<Any?> = presentBlocks().flatMap { it.params() }

    override fun isPresent() = presentBlocks().isNotEmpty()

    protected abstract fun blocks(): List<StatementBlock>

    private fun presentBlocks() = blocks.filter { it.isPresent() }
}
