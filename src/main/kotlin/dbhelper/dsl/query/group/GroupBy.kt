package dbhelper.dsl.query.group

import dbhelper.dsl.fields.Column
import dbhelper.dsl.fields.Field

class GroupBy(private val fields: List<String>): Group {
    constructor(vararg fields: Field): this(fields.map { it.toString() })

    override fun toString() = " GROUP BY ${fields.joinToString(", ") { Column(it).toString() }}"
}
