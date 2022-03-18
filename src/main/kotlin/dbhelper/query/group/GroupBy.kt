package dbhelper.query.group

import dbhelper.query.fields.Fields.Companion.wrap

class GroupBy(private val fields: List<String>): Group {
    override fun toString() = " GROUP BY ${fields.joinToString(", ") { it.wrap() }}"
}
