package dbhelper.query.join

class Joins {
    private val joins: MutableList<Join> = mutableListOf()

    fun statement(): String {
        if(joins.isEmpty()) {
            return ""
        }
        return joins.joinToString(prefix = " ", separator = " ") { it.statement() }
    }
    fun join(on: On) = join(InnerJoin(on))
    fun leftJoin(on: On) = join(LeftJoin(on))
    fun rightJoin(on: On) = join(RightJoin(on))

    private fun join(join: Join) {
        this.joins.add(join)
    }
}