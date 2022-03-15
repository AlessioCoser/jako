package dbhelper.query.order

import dbhelper.query.fields.Fields.Companion.wrap

abstract class Order(private val direction: String, private vararg val fields: String) {
    fun statement() = fields.joinToString(" ${direction}, ", postfix = " $direction") { it.wrap() }
}