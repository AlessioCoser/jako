package dbhelper.dsl

data class Query(val statement: String, val params: List<Any?>)

class QueryBuilder {
    private var from: String = ""
    private var fields: List<String> = listOf("*")
    private var where: WhereCondition = Empty()
    private var joins: MutableList<Join> = mutableListOf()
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
        joins.add(InnerJoin(join))
    }

    fun leftJoin(join: GenericJoin) {
        joins.add(LeftJoin(join))
    }

    fun groupBy(field: String) {
        groupBy = " GROUP BY $field"
    }

    fun build(): Query {
        return Query("SELECT ${joinFields()} FROM $from${joinJoins()} WHERE ${where.statement()}$groupBy", where.params())
    }

    private fun joinJoins(): String {
        return joins.joinToString(separator = " ", prefix = " ") { it.statement() }
    }

    private fun joinFields(): String {
        return fields.joinToString(separator = ", ")
    }
}