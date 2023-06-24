package jako.dsl.query.join

import jako.dsl.Dialect

internal class Joins: Join {
    private val joins: MutableList<Join> = mutableListOf()

    override fun toSQL(dialect: Dialect): String {
        if(joins.isEmpty()) {
            return ""
        }
        return joins.joinToString(separator = " ") { it.toSQL(dialect) }
    }

    override fun params(): List<Any?> {
        return joins.flatMap { it.params() }
    }

    fun join(on: On) = join(InnerJoin(on))
    fun leftJoin(on: On) = join(LeftJoin(on))
    fun rightJoin(on: On) = join(RightJoin(on))

    private fun join(join: Join) {
        this.joins.add(join)
    }
}