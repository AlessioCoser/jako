package jako.dsl.update

import jako.dsl.StatementBlock
import jako.dsl.fields.Column


internal class UpdateTable(private val table: String): StatementBlock {
    override fun toString() = "UPDATE ${Column(table)}"
    override fun params(): List<Any?> = emptyList()
}
