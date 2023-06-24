package jako.dsl.query.order

import jako.dsl.Dialect
import jako.dsl.fields.Column

abstract class OrderField(private val direction: String, private vararg val fields: String) {
    fun toSQL(dialect: Dialect = Dialect.PSQL) = fields.joinToString(" ${direction}, ", postfix = " $direction") { Column(it).toSQL(dialect) }
}