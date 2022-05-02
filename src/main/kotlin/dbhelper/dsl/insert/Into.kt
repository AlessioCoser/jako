package dbhelper.dsl.insert

import dbhelper.dsl.fields.Column


class Into(private val table: String) {
    override fun toString() = " INTO ${Column(table)}"
}
