package jako.dsl.query.group

import jako.dsl.fields.Column
import jako.dsl.fields.Field

internal class GroupBy(private val fields: List<String>): Group {
    constructor(vararg fields: Field): this(fields.map { it.toString() })

    override fun toString() = "GROUP BY ${fields.joinToString(", ") { Column(it).toString() }}"
}
