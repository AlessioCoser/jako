package jako.dsl.query.order

import jako.dsl.fields.Column

abstract class OrderField(private val direction: String, private vararg val fields: String) {
    override fun toString() = fields.joinToString(" ${direction}, ", postfix = " $direction") { Column(it).toString() }
}