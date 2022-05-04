package jako.dsl.query.having

import jako.dsl.conditions.Condition

internal open class GenericHaving(private val condition: Condition): Having {
    override fun toString() = " HAVING $condition"
    override fun params() = condition.params()
}
