package dbhelper.query.conditions

interface Condition {
    override fun toString(): String
    fun params(): List<Any?>
}
