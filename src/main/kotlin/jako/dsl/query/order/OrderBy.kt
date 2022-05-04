package jako.dsl.query.order

internal class OrderBy(private val orderFields: List<OrderField>): Order {
    override fun toString() = " ORDER BY ${orderFields.joinToString(", ") { "$it" }}"
}
