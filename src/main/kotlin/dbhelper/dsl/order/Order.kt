package dbhelper.dsl.order

abstract class Order(private vararg val fields: String) {
    fun statement() = fields.joinToString(", ")
    abstract fun direction(): String
}
