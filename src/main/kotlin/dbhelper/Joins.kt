package dbhelper

class Joins(vararg val joins: Join) {
    fun text(): String {
        if(joins.isEmpty()) {
            return ""
        }

        return joins.joinToString(separator = " ", prefix = " ") { "${it.type()} ${it.text}" }
    }

    fun params(): List<Any> {
        return joins.flatMap { it.params.toList() }
    }
}

interface Join {
    val text: String
    val params: Array<out Any>

    fun type(): String
}

class InnerJoin(override val text: String, override vararg val params: Any): Join {
    override fun type() = "INNER JOIN"
}

class LeftJoin(override val text: String, override vararg val params: Any): Join {
    override fun type() = "LEFT JOIN"
}
