package dbhelper.dsl.conditions

interface Condition {
    fun statement(): String
    fun params(): List<Any?>
}

class Empty: Condition {
    override fun statement() = "true"
    override fun params() = listOf<Any?>()
}
