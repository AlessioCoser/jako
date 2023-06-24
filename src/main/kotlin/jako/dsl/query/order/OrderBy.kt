package jako.dsl.query.order

import jako.dsl.Dialect

internal class OrderBy(private val orderFields: List<OrderField>): Order {
    override fun toSQL(dialect: Dialect) = "ORDER BY ${orderFields.joinToString(", ") { it.toSQL(dialect) }}"
}
