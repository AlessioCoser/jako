package dbhelper.dsl

interface Join {
    fun statement(): String
}

class GenericJoin(private val table: String, private val onCondition: WhereCondition): Join {
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

infix fun String.on(value: WhereCondition): GenericJoin {
    return GenericJoin(this, value)
}