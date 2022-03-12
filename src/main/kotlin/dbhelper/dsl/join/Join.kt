package dbhelper.dsl.join

import dbhelper.dsl.conditions.Condition

abstract class Join (private val type: JoinType, private val table: String, private val condition: Condition) {

    fun statement(): String {
        return "${type.value} $table ON ${compileCondition(condition)}"
    }

    private fun compileCondition(condition: Condition): String {
        val statement = condition.statement()
        val params = condition.params()
        return params.fold(statement) { acc, value -> acc.replaceFirst("?", value.toString()) }
    }
}
