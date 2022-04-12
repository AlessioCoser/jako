package dbhelper.dsl.query.having

interface Having {
    override fun toString(): String
    fun params(): List<Any?>
}
