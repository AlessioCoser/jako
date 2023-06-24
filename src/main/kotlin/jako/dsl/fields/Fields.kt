package jako.dsl.fields

import jako.dsl.Dialect


internal class Fields(private vararg val fields: Field): Field {
    constructor(vararg fields: String): this(*(fields.map { Column(it) }).toTypedArray())

    override fun toSQL(dialect: Dialect) = fields.joinToString(separator = ", ") { it.toSQL(dialect) }
    override fun params(): List<Any?> = fields.flatMap { it.params() }
}
