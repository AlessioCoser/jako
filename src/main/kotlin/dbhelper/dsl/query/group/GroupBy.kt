package dbhelper.dsl.query.group

import dbhelper.dsl.fields.Fields.Companion.wrap

class GroupBy(private val fields: List<String>): Group {
    override fun toString() = " GROUP BY ${fields.joinToString(", ") { it.wrap() }}"
}
