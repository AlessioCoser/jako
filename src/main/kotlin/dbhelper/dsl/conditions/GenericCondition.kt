package dbhelper.dsl.conditions

import dbhelper.dsl.fields.Fields.Companion.wrap

abstract class GenericCondition(
    private val left: String,
    private val operator: String,
    private val right: Any?
) : Condition {
    override fun toString(): String {
        return "${left.wrap()} $operator ?"
    }

    override fun params() = listOf(right)
}
