package dbhelper.dsl

import dbhelper.dsl.conditions.Condition
import dbhelper.dsl.conditions.Empty

data class Query(val statement: String, val params: List<Any?>)

class QueryBuilder {
    private var from: String = ""
    private var fields: List<String> = listOf("*")
    private var where: Condition = Empty()
    private var joins: MutableList<Join> = mutableListOf()
    private var groupBy: String = ""

    fun from(table: String): QueryBuilder {
        this.from = table
        return this
    }

    fun fields(vararg fields: String): QueryBuilder {
        this.fields = fields.toList()
        return this
    }

    fun where(condition: Condition): QueryBuilder {
        where = condition
        return this
    }

    fun join(join: GenericJoin): QueryBuilder {
        joins.add(InnerJoin(join))
        return this
    }

    fun leftJoin(join: GenericJoin): QueryBuilder {
        joins.add(LeftJoin(join))
        return this
    }

    fun groupBy(field: String): QueryBuilder {
        groupBy = " GROUP BY $field"
        return this
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