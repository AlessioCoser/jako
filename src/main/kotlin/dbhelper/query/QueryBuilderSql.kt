package dbhelper.query

import dbhelper.query.conditions.Condition
import dbhelper.query.join.Join
import dbhelper.query.join.JoinBuilder
import dbhelper.query.order.Order

class QueryBuilderSql : QueryBuilder {
    private var from: String = ""
    private var fields: List<String> = listOf("*")
    private var where: String = ""
    private var whereParams: List<Any?> = emptyList()
    private var join: JoinBuilder = JoinBuilder()
    private var groupBy: String = ""
    private var having: String = ""
    private var havingParams: List<Any?> = emptyList()
    private var orderBy: String = ""
    private var limit: String = ""
    private var raw: String = ""

    fun raw(statement: String): QueryBuilderSql {
        raw = statement
        return this
    }

    fun from(table: String): QueryBuilderSql {
        this.from = " FROM $table"
        return this
    }

    fun fields(vararg fields: String): QueryBuilderSql {
        this.fields = fields.toList()
        return this
    }

    fun where(condition: Condition): QueryBuilderSql {
        where = " WHERE ${condition.statement()}"
        whereParams = condition.params()
        return this
    }

    fun join(join: Join): QueryBuilderSql {
        this.join.add(join)
        return this
    }

    fun orderBy(vararg order: Order): QueryBuilderSql {
        orderBy = " ORDER BY ${order.joinToString(", ") { it.statement() }}"
        return this
    }

    fun groupBy(vararg fields: String): QueryBuilderSql {
        groupBy = " GROUP BY ${fields.joinToString(", ")}"
        return this
    }

    fun having(condition: Condition): QueryBuilderSql {
        having = " HAVING ${condition.statement()}"
        havingParams = condition.params()
        return this
    }

    fun limit(limit: Int, offset: Int = 0): QueryBuilderSql {
        if(offset != 0) {
            this.limit = " LIMIT $limit OFFSET $offset"
        } else {
            this.limit = " LIMIT $limit"
        }
        return this
    }

    override fun single() = limit(1)

    override fun build(): Query {
        if (raw.isNotBlank()) {
            return Query(raw, emptyList())
        }

        if(from.isBlank()) {
            throw RuntimeException("Cannot generate query without table name")
        }

        return Query(
            "SELECT ${joinFields()}$from${join.build()}$where$groupBy$having$orderBy$limit",
            whereParams.plus(havingParams)
        )
    }

    private fun joinFields(): String {
        return fields.joinToString(separator = ", ")
    }
}
