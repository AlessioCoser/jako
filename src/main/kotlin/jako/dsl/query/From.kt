package jako.dsl.query

import jako.dsl.Dialect
import jako.dsl.StatementBlock
import jako.dsl.fields.Column

internal class From(private val table: String): StatementBlock {
    override fun toSQL(dialect: Dialect) = "FROM ${Column(table).toSQL(dialect)}"
    override fun params(): List<Any?> = emptyList()
}
