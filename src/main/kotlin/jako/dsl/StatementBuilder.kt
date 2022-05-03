package jako.dsl

abstract class StatementBuilder internal constructor(): Statement {
    private val blocks: List<StatementBlock> by lazy { blocks() }

    override fun toString(): String = blocks.joinToString("") { it.toString() }
    override fun params(): List<Any?> = blocks.flatMap { it.params() }

    protected abstract fun blocks(): List<StatementBlock>
}
