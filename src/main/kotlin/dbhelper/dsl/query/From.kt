package dbhelper.dsl.query

import dbhelper.dsl.fields.Fields.Companion.wrap

class From(private val table: String) {
    override fun toString() = " FROM ${table.wrap()}"
}
