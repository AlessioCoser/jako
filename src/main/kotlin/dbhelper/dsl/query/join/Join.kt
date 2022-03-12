package dbhelper.dsl.query.join

import dbhelper.dsl.query.conditions.Condition

abstract class Join (private val type: String, private val table: String, private val condition: Condition) {

    fun statement(): String {
        return "$type $table ON ${compileCondition(condition)}"
    }

    private fun compileCondition(condition: Condition): String {
        val statement = condition.statement()
        val params = condition.params()
        return params.fold(statement) { acc, value -> acc.replaceFirst("?", value.toString()) }
    }
}
