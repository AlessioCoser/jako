package dbhelper.insert

import dbhelper.query.fields.Fields.Companion.wrap

class Values(private vararg val columns: Column) {
    private val params = transpose(*columns)

    fun columns(): String {
        return columns.joinToString(prefix = " (", separator = ", ", postfix = ")") { it.name.wrap() }
    }

    fun values(): String {
        return " VALUES " + insertPlaceHolders()
    }

    fun params(): List<Any?> {
        return params
    }

    private fun insertPlaceHolders(): String {
        val rowSize = params.size / columns.size
        val columnsString = columns.indices.joinToString(", ") { "?" }

        return (0 until rowSize).joinToString(separator = "), (", prefix = "(", postfix = ")") { columnsString }
    }

    private fun transpose(vararg columns: Column): List<Any?> {
        val columnIndices = columns.indices
        val maxRowSize = columns.maxBy { it.size }?.size ?: 0
        val rowIndices = 0 until maxRowSize

        return rowIndices.flatMap { columnIndex ->
            columnIndices.map { rowIndex ->
                columns.getOrNull(rowIndex)?.getOrNull(columnIndex)
            }
        }
    }
}
