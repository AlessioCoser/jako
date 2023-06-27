package jako.dsl.update

import jako.dsl.Dialect
import jako.dsl.StatementBlock

internal class SetFields: StatementBlock {
    private val cols: MutableList<SetField> = mutableListOf()

    fun add(column: SetField): SetFields {
        cols.add(column)
        return this
    }

    override fun toSQL(dialect: Dialect) = "SET " + presentCols().joinToString(", ") { it.toSQL(dialect)}
    override fun params() = presentCols().flatMap { it.params() }
    override fun isPresent() = presentCols().isNotEmpty()

    private fun presentCols() = cols.filter { it.isPresent() }
}
