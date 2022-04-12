package dbhelper.dsl.conditions

interface Condition {
    override fun toString(): String
    fun params(): List<Any?>
}
