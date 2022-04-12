package dbhelper.dsl

import dbhelper.dsl.fields.Fields.Companion.wrap

class Into(private val table: String) {
    override fun toString() = " INTO ${table.wrap()}"
}
