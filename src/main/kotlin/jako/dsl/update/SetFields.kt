package jako.dsl.update

import jako.dsl.Dialect
import jako.dsl.StatementBlock

internal class SetFields: StatementBlock {
    private val cols: MutableList<SetField> = mutableListOf()

    fun add(column: SetField): SetFields {
        cols.add(column)
        return this
    }

    override fun toSQL(dialect: Dialect) = if (cols.isNotEmpty()) "SET " + cols.joinToString(", ") { it.toSQL(dialect)} else ""
    override fun params() = cols.flatMap { it.params() }
}
