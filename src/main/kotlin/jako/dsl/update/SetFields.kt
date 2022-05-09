package jako.dsl.update

import jako.dsl.StatementBlock

internal class SetFields: StatementBlock {
    private val cols: MutableList<SetField> = mutableListOf()

    fun add(column: SetField): SetFields {
        cols.add(column)
        return this
    }

    override fun toString() = if (cols.isNotEmpty()) "SET " + cols.joinToString(", ") else ""
    override fun params() = cols.flatMap { it.params() }
}
