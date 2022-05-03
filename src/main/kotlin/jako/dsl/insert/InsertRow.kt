package jako.dsl.insert

import jako.dsl.StatementBlock

class InsertRow: StatementBlock {
    private val cols: MutableList<InsertColumn> = mutableListOf()

    fun add(column: InsertColumn): InsertRow {
        cols.add(column)
        return this
    }

    fun isNotEmpty() = cols.isNotEmpty()

    override fun toString() = " (${columnNames()}) VALUES (${placeholders()})"
    override fun params(): List<Any?> = cols.map { it.value }

    private fun columnNames() = cols.joinToString(prefix = "\"", separator = "\", \"", postfix = "\"") { it.name }

    private fun placeholders() = List(cols.size) { "?" }.joinToString(", ")
}
