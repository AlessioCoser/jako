package dbhelper.dsl.query.conditions

class True : Condition {
    override fun statement() = "true"
    override fun params() = listOf<Any?>()
}
