package dbhelper.dsl

import dbhelper.dsl.conditions.Condition
import dbhelper.dsl.conditions.Empty

data class Query(val statement: String, val params: List<Any?>) {
    class Builder {
        var from2: String = ""
        var fields2: List<String> = listOf("*")
        var where2: Condition = Empty()

        private var from: String = ""
        private var fields: List<String> = listOf("*")
        private var where: Condition = Empty()
        private var joins: MutableList<JoinStatement> = mutableListOf()
        private var groupBy: String = ""
        private var orderBy: String = ""
        private var limit: String = ""
        private var raw: String = ""

        fun raw(statement: String): Builder {
            raw = statement
            return this
        }

        fun from(table: String): Builder {
            this.from = table
            return this
        }

        fun fields(vararg fields: String): Builder {
            this.fields = fields.toList()
            return this
        }

        fun where(condition: Condition): Builder {
            where = condition
            return this
        }

        fun join(join: Join): Builder {
            joins.add(InnerJoin(join))
            return this
        }

        fun leftJoin(join: Join): Builder {
            joins.add(LeftJoin(join))
            return this
        }

        fun orderBy(order: Order): Builder {
            orderBy = " ORDER BY ${order.statement()} ${order.direction()}"
            return this
        }

        fun groupBy(vararg fields: String): Builder {
            groupBy = " GROUP BY ${fields.joinToString(", ")}"
            return this
        }

        fun limit(field: Int): Builder {
            limit = " LIMIT $field"
            return this
        }

        fun build(): Query {
            if (raw.isNotBlank()) {
                return Query(raw, emptyList())
            }
            return Query("SELECT ${joinFields()} FROM $from${joinJoins()} WHERE ${where.statement()}$groupBy$orderBy$limit", where.params())
        }

        private fun joinJoins(): String {
            return joins.joinToString(separator = " ", prefix = " ") { it.statement() }
        }

        private fun joinFields(): String {
            return fields.joinToString(separator = ", ")
        }
    }

    companion object {
        @JvmStatic
        fun builder() = Builder()
    }
}
