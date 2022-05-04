package jako.dsl.update

import jako.dsl.StatementBlock

internal class SetFields: StatementBlock {
    private val cols: MutableList<SetField> = mutableListOf()

    fun add(column: SetField): SetFields {
        cols.add(column)
        return this
    }

    fun isNotEmpty() = cols.isNotEmpty()

    override fun toString() = " SET " + cols.joinToString(", ")
    override fun params() = cols.flatMap { it.params() }
}
