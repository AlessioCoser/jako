package jako.dsl.insert

import jako.dsl.Dialect
import jako.dsl.StatementBlock
import jako.dsl.fields.Column


internal class InsertInto(private val table: String): StatementBlock {
    override fun toSQL(dialect: Dialect) = "INSERT INTO ${Column(table).toSQL(dialect)}"
    override fun params(): List<Any?> = emptyList()
    override fun isPresent() = table.isNotBlank()
}
