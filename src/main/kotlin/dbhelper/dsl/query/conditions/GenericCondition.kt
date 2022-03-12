package dbhelper.dsl.query.conditions

abstract class GenericCondition(
    private val left: String,
    private val operator: String,
    private val right: Any?
) : Condition {
    override fun statement(): String {
        return "$left $operator ?"
    }

    override fun params() = listOf(right)
}
