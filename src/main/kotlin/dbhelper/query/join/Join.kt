package dbhelper.query.join

import dbhelper.query.conditions.Eq

abstract class Join(private val type: String, private val table: String, private val eq: Eq) {

    fun statement(): String {
        return "$type $table ON ${compileCondition(eq)}"
    }

    private fun compileCondition(eq: Eq): String {
        val statement = eq.statement()
        val params = eq.params()
        return params.fold(statement) { acc, value -> acc.replaceFirst("?", value.toString()) }
    }
}
