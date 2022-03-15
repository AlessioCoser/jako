package dbhelper.query.order

import dbhelper.query.Fields.wrap

abstract class Order(private val direction: String, private vararg val fields: String) {
    fun statement() = fields.joinToString(" ${direction}, ", postfix = " $direction") { it.wrap() }
}