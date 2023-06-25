package jako.dsl.query.group

import jako.dsl.Dialect
import jako.dsl.fields.Column
import jako.dsl.fields.Field

internal class GroupBy(private val fields: List<Field>): Group {
    constructor(vararg fields: Field): this(fields.toList())
    constructor(vararg fields: String): this(fields.map { Column(it) })

    override fun toSQL(dialect: Dialect) = "GROUP BY ${fields.joinToString(", ") { it.toSQL(dialect) }}"
    override fun isPresent() = fields.any { it.isPresent() }
}
