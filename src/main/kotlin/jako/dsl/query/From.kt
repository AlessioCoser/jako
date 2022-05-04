package jako.dsl.query

import jako.dsl.StatementBlock
import jako.dsl.fields.Column

internal class From(private val table: String): StatementBlock {
    override fun toString() = " FROM ${Column(table)}"
    override fun params(): List<Any?> = emptyList()
}
