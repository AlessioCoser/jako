package jako.dsl.query.order

import jako.dsl.Dialect

class NoOrder: Order {
    override fun toSQL(dialect: Dialect) = ""
    override fun isPresent() = false
}
