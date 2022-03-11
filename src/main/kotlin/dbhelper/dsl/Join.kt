package dbhelper.dsl

import dbhelper.dsl.conditions.Condition

interface Join {
    fun statement(): String
}

infix fun String.on(value: Condition): GenericJoin {
    return GenericJoin(this, value)
}

class GenericJoin(private val table: String, private val onCondition: Condition): Join {
    override fun statement() = "JOIN $table ON ${compileWhereCondition()}"

    private fun compileWhereCondition(): String {
        val statement = onCondition.statement()
        val params = onCondition.params()
        return params.fold(statement) { acc, value -> acc.replaceFirst("?", value.toString()) }
    }
}

open class TypedJoin(private val type: String, private val joinCondition: Join): Join {
    override fun statement() = "$type ${joinCondition.statement()}"
}

class InnerJoin(join: Join): TypedJoin("INNER", join)
class LeftJoin(join: Join): TypedJoin("LEFT", join)
