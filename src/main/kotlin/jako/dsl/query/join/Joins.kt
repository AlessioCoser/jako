package jako.dsl.query.join

internal class Joins: Join {
    private val joins: MutableList<Join> = mutableListOf()

    override fun toString(): String {
        if(joins.isEmpty()) {
            return ""
        }
        return joins.joinToString(separator = " ") { "$it" }
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