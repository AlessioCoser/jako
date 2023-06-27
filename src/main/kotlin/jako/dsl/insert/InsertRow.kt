package jako.dsl.insert

import jako.dsl.Dialect
import jako.dsl.StatementBlock

internal class InsertRow: StatementBlock {
    private val cols: MutableList<InsertColumn> = mutableListOf()

    fun add(column: InsertColumn): InsertRow {
        cols.add(column)
        return this
    }

    override fun toSQL(dialect: Dialect) = "(${columnNames(dialect)}) VALUES (${placeholders()})"
    override fun params(): List<Any?> = presentCols().map { it.value }
    override fun isPresent() = presentCols().isNotEmpty()

    private fun columnNames(dialect: Dialect) = presentCols().joinToString(prefix = dialect.columnSeparator, separator = "${dialect.columnSeparator}, ${dialect.columnSeparator}", postfix = dialect.columnSeparator) { it.name }

    private fun placeholders() = List(presentCols().size) { "?" }.joinToString(", ")

    private fun presentCols() = cols.filter { it.isPresent() }
}
