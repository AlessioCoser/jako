package jako.dsl.fields

import jako.dsl.Dialect


internal class Fields(private vararg val fields: Field): Field {
    constructor(vararg fields: String): this(*(fields.map { Column(it) }).toTypedArray())

    override fun toSQL(dialect: Dialect): String {
        if (presentFields().isEmpty())
            return ALL.toSQL(dialect)
        return presentFields().joinToString(separator = ", ") { it.toSQL(dialect) }
    }
    override fun params(): List<Any?> = presentFields().flatMap { it.params() }
    override fun isPresent() = true

    private fun presentFields() = fields.filter { it.isPresent() }
}
