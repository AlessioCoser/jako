package dbhelper.query.order

import dbhelper.query.fields.Fields.Companion.wrap

abstract class OrderField(private val direction: String, private vararg val fields: String) {
    override fun toString() = fields.joinToString(" ${direction}, ", postfix = " $direction") { it.wrap() }
}