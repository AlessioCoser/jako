package dbhelper.query

import dbhelper.query.fields.Fields.Companion.wrap

class From(private val table: String) {
    fun statement() = " FROM ${table.wrap()}"
}
