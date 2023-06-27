package jako.dsl.query.having

import jako.dsl.Dialect
import jako.dsl.conditions.Condition

internal open class GenericHaving(private val condition: Condition): Having {
    override fun toSQL(dialect: Dialect) = "HAVING ${condition.toSQL(dialect)}"
    override fun params() = if (isPresent()) condition.params() else emptyList()
    override fun isPresent() = condition.isPresent()
}
