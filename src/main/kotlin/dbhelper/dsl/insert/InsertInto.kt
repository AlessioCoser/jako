package dbhelper.dsl.insert

import dbhelper.dsl.StatementBlock
import dbhelper.dsl.fields.Column


class InsertInto(private val table: String): StatementBlock {
    override fun toString() = "INSERT INTO ${Column(table)}"
    override fun params(): List<Any?> = emptyList()
}
