package dbhelper.dsl.update

import dbhelper.dsl.StatementBlock
import dbhelper.dsl.fields.Column


class UpdateTable(private val table: String): StatementBlock {
    override fun toString() = "UPDATE ${Column(table)}"
    override fun params(): List<Any?> = emptyList()
}
