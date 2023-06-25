package jako.dsl.update

import jako.dsl.Dialect
import jako.dsl.StatementBlock
import jako.dsl.fields.Column


internal class UpdateTable(private val table: String): StatementBlock {
    override fun toSQL(dialect: Dialect) = "UPDATE ${Column(table).toSQL(dialect)}"
    override fun params(): List<Any?> = emptyList()
    override fun isPresent() = table.isNotBlank()
}
