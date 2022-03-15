package dbhelper.query.conditions

import dbhelper.query.fields.Fields.Companion.wrap

abstract class GenericCondition(
    private val left: String,
    private val operator: String,
    private val right: Any?
) : Condition {
    override fun statement(): String {
        return "${left.wrap()} $operator ?"
    }

    override fun params() = listOf(right)
}
