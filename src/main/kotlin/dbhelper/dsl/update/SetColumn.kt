package dbhelper.dsl.update

import dbhelper.dsl.fields.Column

data class SetColumn(private val name: String, val value: Any?) {
    override fun toString() = "${Column(name)} = ?"
}
