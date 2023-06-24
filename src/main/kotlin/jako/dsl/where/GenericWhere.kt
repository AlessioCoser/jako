package jako.dsl.where

import jako.dsl.Dialect
import jako.dsl.conditions.Condition

internal open class GenericWhere(private val condition: Condition): Where {
    override fun toSQL(dialect: Dialect) = "WHERE ${condition.toSQL(dialect)}"
    override fun params() = condition.params()
}
