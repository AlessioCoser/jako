package dbhelper.dsl.where

import dbhelper.dsl.conditions.Condition

open class GenericWhere(private val condition: Condition): Where {
    override fun toString() = " WHERE $condition"
    override fun params() = condition.params()
}
