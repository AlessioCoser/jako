package dbhelper.dsl.query

import dbhelper.dsl.StatementBlock
import dbhelper.dsl.fields.Column

class From(private val table: String): StatementBlock {
    override fun toString() = " FROM ${Column(table)}"
    override fun params(): List<Any?> = emptyList()
}
