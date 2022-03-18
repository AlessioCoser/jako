package dbhelper.query.having

import dbhelper.query.conditions.Condition

open class GenericHaving(private val condition: Condition): Having {
    override fun toString() = " HAVING $condition"
    override fun params() = condition.params()
}
