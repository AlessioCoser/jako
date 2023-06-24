package jako.dsl.conditions

import jako.dsl.Dialect
import jako.dsl.fields.Field

abstract class GenericCondition(
    private val left: Field,
    private val operator: String,
    private val right: Any?
) : Condition {
    override fun toSQL(dialect: Dialect) =  "${left.toSQL(dialect)} $operator ?"
    override fun params() = left.params() + right
}
