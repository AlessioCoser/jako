package dbhelper

class Where(vararg val conditions: Condition) {
    fun text(): String {
        if (conditions.isEmpty()) {
            return ""
        }

        val string = conditions.mapIndexed { index, condition ->
            if (index > 0) {
                "${condition.type()} ${condition.text}"
            } else {
                condition.text
            }
        }.joinToString(separator = " ")

        return " WHERE $string"
    }

    fun params(): List<Any> {
        return conditions.flatMap { it.params.toList() }
    }
}

interface Condition {
    val text: String
    val params: Array<out Any>

    fun type(): String
}

class And(override val text: String, override vararg val params: Any) : Condition {
    override fun type() = "AND"
}
