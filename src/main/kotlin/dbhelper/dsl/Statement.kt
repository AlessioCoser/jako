package dbhelper.dsl

interface Statement {
    override fun toString(): String
    fun params(): List<Any?>
}
