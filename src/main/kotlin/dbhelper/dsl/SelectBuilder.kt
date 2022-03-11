package dbhelper.dsl

interface JoinCondition {
    fun statement(): String
}

class GenericJoin(private val table: String, private val whereCondition: WhereCondition): JoinCondition {
    override fun statement() = "JOIN $table ON ${compileWhereCondition()}"

    private fun compileWhereCondition(): String {
        val statement = whereCondition.statement()
        val params = whereCondition.params()
        return params.fold(statement) { acc, value -> acc.replaceFirst("?", value.toString()) }
    }
}

open class TypedJoin(private val type: String, private val joinCondition: JoinCondition): JoinCondition {
    override fun statement() = "$type ${joinCondition.statement()}"
}

class InnerJoin2(joinCondition: JoinCondition): TypedJoin("INNER", joinCondition)
class LeftJoin2(joinCondition: JoinCondition): TypedJoin("LEFT", joinCondition)

infix fun String.on(value: WhereCondition): GenericJoin {
    return GenericJoin(this, value)
}

data class Select2(val statement: String, val params: List<Any?>)

class SelectBuilder {
    private var from: String = ""
    private var fields: List<String> = listOf("*")
    private var where: WhereCondition = Empty()
    private var joins: MutableList<JoinCondition> = mutableListOf()
    private var groupBy: String = ""

    fun from(table: String) {
        this.from = table
    }

    fun fields(vararg fields: String) {
        this.fields = fields.toList()
    }

    fun where(condition: WhereCondition) {
        where = condition
    }

    fun join(join: GenericJoin) {
        joins.add(InnerJoin2(join))
    }

    fun leftJoin(join: GenericJoin) {
        joins.add(LeftJoin2(join))
    }

    fun groupBy(field: String) {
        groupBy = " GROUP BY $field"
    }

    fun build(): Select2 {
        return Select2("SELECT ${joinFields()} FROM $from${joinJoins()} WHERE ${where.statement()}$groupBy", where.params())
    }

    private fun joinJoins(): String {
        return joins.joinToString(separator = " ", prefix = " ") { it.statement() }
    }

    private fun joinFields(): String {
        return fields.joinToString(separator = ", ")
    }
}