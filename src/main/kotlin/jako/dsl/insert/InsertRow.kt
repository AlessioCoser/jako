package jako.dsl.insert

import jako.dsl.Dialect
import jako.dsl.StatementBlock

internal class InsertRow: StatementBlock {
    private val cols: MutableList<InsertColumn> = mutableListOf()

    fun add(column: InsertColumn): InsertRow {
        cols.add(column)
        return this
    }

    override fun toSQL(dialect: Dialect) = if(cols.isNotEmpty()) "(${columnNames(dialect)}) VALUES (${placeholders()})" else ""
    override fun params(): List<Any?> = cols.map { it.value }

    private fun columnNames(dialect: Dialect) = cols.joinToString(prefix = "${dialect.columnSeparator}", separator = "${dialect.columnSeparator}, ${dialect.columnSeparator}", postfix = "${dialect.columnSeparator}") { it.name }

    private fun placeholders() = List(cols.size) { "?" }.joinToString(", ")
}
