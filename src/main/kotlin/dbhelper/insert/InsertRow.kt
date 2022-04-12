package dbhelper.insert

class InsertRow {
    private val cols: MutableList<InsertColumn> = mutableListOf()

    fun add(column: InsertColumn): InsertRow {
        cols.add(column)
        return this
    }

    fun isNotEmpty() = cols.isNotEmpty()

    fun statement(): String {
        return "(${columnNames()}) VALUES (${placeholders()})"
    }

    fun params(): List<Any?> = cols.map { it.value }

    private fun columnNames() = cols.joinToString(prefix = "\"", separator = "\", \"", postfix = "\"") { it.name }

    private fun placeholders() = List(cols.size) { "?" }.joinToString(", ")
}
