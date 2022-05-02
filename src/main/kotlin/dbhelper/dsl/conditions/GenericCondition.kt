package dbhelper.dsl.conditions

import dbhelper.dsl.fields.Field

abstract class GenericCondition(
    private val left: Field,
    private val operator: String,
    private val right: Any?
) : Condition {
    override fun toString() = "$left $operator ?"
    override fun params() = left.params() + right
}
