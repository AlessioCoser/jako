package dbhelper.dsl.conditions

interface Condition {
    fun statement(): String
    fun params(): List<Any?>
}
