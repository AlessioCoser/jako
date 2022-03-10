package dbhelper

class Where(vararg val conditions: Condition) {
    fun text(): String {
        if (conditions.isEmpty()) {
            return ""
        }

        return " WHERE true ${conditions.joinToString(separator = " ") { "${it.type()} ${it.text}" }}"
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
