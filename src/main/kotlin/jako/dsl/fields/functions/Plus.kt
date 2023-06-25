package jako.dsl.fields.functions

import jako.dsl.Dialect
import jako.dsl.fields.Field

class Plus(private val left: Field, private val right: Int): Field {
    override fun toSQL(dialect: Dialect) = "${left.toSQL(dialect)} + $right"
    override fun params() = left.params()
    override fun isPresent() = left.isPresent()
}