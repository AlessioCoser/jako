package dbhelper.dsl

interface Statement: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?>
}
