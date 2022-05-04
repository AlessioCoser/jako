package jako.dsl.query.having

internal class NoHaving: Having {
    override fun toString() = ""
    override fun params() = emptyList<String>()
}
