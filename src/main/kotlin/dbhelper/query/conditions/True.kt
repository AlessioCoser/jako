package dbhelper.query.conditions

class True : Condition {
    override fun statement() = "true"
    override fun params() = listOf<Any?>()
}
