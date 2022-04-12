package dbhelper.insert

import dbhelper.query.fields.Fields.Companion.wrap

class InsertRow(cols: List<InsertColumn>) {
    val columns = cols.associate { it.name to it.value }
}

class InsertValues {
    private val rows: MutableList<InsertRow> = mutableListOf()
    private val columns: List<String> by lazy { initializeColumns() }

    fun add(row: InsertRow) {
        rows.add(row)
    }

    fun params(): List<Any?> {
        return rows.flatMap { row -> columns.map { row.columns[it] } }
    }

    fun statement(): String {
        if(rows.isEmpty()) {
            throw RuntimeException("Cannot generate insert without values")
        }
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
