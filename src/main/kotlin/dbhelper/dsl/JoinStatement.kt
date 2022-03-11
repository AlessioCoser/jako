package dbhelper.dsl

import dbhelper.dsl.conditions.Condition

infix fun String.on(value: Condition): Join {
    return Join(this, value)
}

interface JoinStatement {
    fun statement(): String
}

class Join(private val table: String, private val onCondition: Condition): JoinStatement {
    override fun statement() = "JOIN $table ON ${compileWhereCondition()}"

    private fun compileWhereCondition(): String {
        val statement = onCondition.statement()
        val params = onCondition.params()
        return params.fold(statement) { acc, value -> acc.replaceFirst("?", value.toString()) }
    }
}

open class TypedJoin(private val type: String, private val joinCondition: JoinStatement): JoinStatement {
    override fun statement() = "$type ${joinCondition.statement()}"
}

class InnerJoin(join: JoinStatement): TypedJoin("INNER", join)

class LeftJoin(join: JoinStatement): TypedJoin("LEFT", join)
