package jako.dsl

interface Statement: StatementBlock {
    override fun toSQL(dialect: Dialect): String
    override fun params(): List<Any?>
}
