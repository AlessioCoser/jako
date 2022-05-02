package dbhelper.dsl.query

import dbhelper.dsl.fields.Column

class From(private val table: String) {
    override fun toString() = " FROM ${Column(table)}"
}
