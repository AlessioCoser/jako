package dbhelper.query

import dbhelper.query.fields.Fields.Companion.wrap

class From(private val table: String) {
    override fun toString() = " FROM ${table.wrap()}"
}
