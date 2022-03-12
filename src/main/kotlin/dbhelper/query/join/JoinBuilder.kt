package dbhelper.query.join

class JoinBuilder {
    private var joins: MutableList<Join> = mutableListOf()

    fun add(join: Join) {
        joins.add(join)
    }

    fun build(): String {
        if(joins.isEmpty()) {
            return ""
        }
        return joins.joinToString(separator = " ", prefix = " ") { it.statement() }
    }
}