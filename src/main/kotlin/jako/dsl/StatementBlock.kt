package jako.dsl

interface StatementBlock {
    fun toSQL(dialect: Dialect = Dialect.PSQL): String
    fun params(): List<Any?>

    fun isPresent(dialect: Dialect = Dialect.PSQL): Boolean {
        return toSQL(dialect).isNotEmpty()
    }
}
