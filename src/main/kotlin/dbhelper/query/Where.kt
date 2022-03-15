package dbhelper.query

import dbhelper.query.conditions.Condition

interface Where {
    fun statement(): String
    fun params(): List<Any?>
}

open class WhereStatement(private val condition: Condition): Where {
    override fun statement() = " WHERE ${condition.statement()}"
    override fun params() = condition.params()
}

class EmptyWhere: Where {
    override fun statement() = ""
    override fun params() = emptyList<String>()
}


