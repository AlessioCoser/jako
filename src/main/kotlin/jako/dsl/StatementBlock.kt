package jako.dsl

interface StatementBlock {
    override fun toString(): String
    fun params(): List<Any?>

    fun isPresent(): Boolean {
        return toString().isNotEmpty()
    }
}
