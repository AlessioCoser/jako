package dbhelper.query.order

abstract class Order(private val direction: String, private vararg val fields: String) {
    fun statement() = fields.joinToString(" ${direction}, ", postfix = " $direction")
}