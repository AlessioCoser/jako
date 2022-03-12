package dbhelper.dsl.query.conditions

interface Condition {
    fun statement(): String
    fun params(): List<Any?>
}
