package dbhelper.insert

import dbhelper.query.fields.Fields.Companion.wrap

class Row(cols: List<Column>) {
    val columns = cols.associate { it.name to it.value }
}

class Values {
    private val rows: MutableList<Row> = mutableListOf()
    private val columns: List<String> by lazy { initializeColumns() }

    fun add(row: Row) {
        rows.add(row)
    }

    fun params(): List<Any?> {
        return rows.flatMap { row -> columns.map { row.columns[it] } }
    }

    fun statement(): String {
        val columnsSql = columns.joinToString(prefix = "(", separator = ", ", postfix = ")") { it.wrap() }
        return " $columnsSql VALUES " + insertPlaceHolders()
    }

    private fun insertPlaceHolders(): String {
        val placeholders = columns.indices.joinToString(", ") { "?" }

        return (0 until rows.size).joinToString(separator = "), (", prefix = "(", postfix = ")") { placeholders }
    }

    private fun initializeColumns(): List<String> {
        return rows.flatMap { row -> row.columns.keys }.distinct()
    }
}
