package dbhelper.query.conditions

import dbhelper.query.Fields.wrap

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
