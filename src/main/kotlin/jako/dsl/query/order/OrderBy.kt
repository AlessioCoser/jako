package jako.dsl.query.order

class OrderBy(private val orderFields: List<OrderField>): Order {
    override fun toString() = " ORDER BY ${orderFields.joinToString(", ") { "$it" }}"
}
