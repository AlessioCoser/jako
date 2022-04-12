package dbhelper.dsl.update

import dbhelper.dsl.fields.Fields.Companion.wrap

data class SetColumn(private val name: String, val value: Any?) {
    override fun toString() = "${name.wrap()} = ?"
}
