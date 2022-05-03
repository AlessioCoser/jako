package jako.dsl.insert

import jako.dsl.StatementBlock
import jako.dsl.fields.Column


class InsertInto(private val table: String): StatementBlock {
    override fun toString() = "INSERT INTO ${Column(table)}"
    override fun params(): List<Any?> = emptyList()
}
