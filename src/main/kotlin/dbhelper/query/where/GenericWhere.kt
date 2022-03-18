package dbhelper.query.where

import dbhelper.query.conditions.Condition

open class GenericWhere(private val condition: Condition): Where {
    override fun toString() = " WHERE $condition"
    override fun params() = condition.params()
}
