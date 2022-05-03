package dbhelper.dsl.insert

import dbhelper.dsl.StatementBlock
import dbhelper.dsl.fields.Column


class Into(private val table: String): StatementBlock {
    override fun toString() = " INTO ${Column(table)}"
    override fun params(): List<Any?> = emptyList()
}
