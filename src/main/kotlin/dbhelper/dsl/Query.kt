package dbhelper.dsl

import dbhelper.dsl.conditions.Condition
import dbhelper.dsl.conditions.Empty

data class Query(val statement: String, val params: List<Any?>) {
    class Builder {
        private var from: String = ""
        private var fields: List<String> = listOf("*")
        private var where: Condition = Empty()
        private var joins: MutableList<Join> = mutableListOf()
        private var groupBy: String = ""
        private var limit: String = ""

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

        fun join(join: GenericJoin): Builder {
            joins.add(InnerJoin(join))
            return this
        }

        fun leftJoin(join: GenericJoin): Builder {
            joins.add(LeftJoin(join))
            return this
        }

        fun groupBy(field: String): Builder {
            groupBy = " GROUP BY $field"
            return this
        }

        fun limit(field: Int): Builder {
            limit = " LIMIT $field"
            return this
        }

        fun build(): Query {
            return Query("SELECT ${joinFields()} FROM $from${joinJoins()} WHERE ${where.statement()}$groupBy$limit", where.params())
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
