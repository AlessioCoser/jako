package jako.dsl.where

import jako.dsl.conditions.Condition

internal open class GenericWhere(private val condition: Condition): Where {
    override fun toString() = " WHERE $condition"
    override fun params() = condition.params()
}
