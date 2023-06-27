package jako.dsl

import jako.dsl.fields.Column
import jako.dsl.fields.Field


internal class Returning(private vararg val fields: Field): StatementBlock {
    constructor(): this(*emptyList<Field>().toTypedArray())
    constructor(vararg fields: String): this(*(fields.map { Column(it) }).toTypedArray())

    override fun toSQL(dialect: Dialect): String {
        if (dialect == Dialect.MYSQL) {
            throw RuntimeException("Cannot use RETURNING statement with MYSQL dialect")
        }
        return "RETURNING ${presentFields().joinToString(", ") { it.toSQL(dialect) }}"
    }

    override fun params(): List<Any?> = presentFields().flatMap { it.params() }
    override fun isPresent() = presentFields().isNotEmpty()

    private fun presentFields() = fields.filter { it.isPresent() }
}
