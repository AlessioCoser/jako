package jako.dsl.query.having

class NoHaving: Having {
    override fun toString() = ""
    override fun params() = emptyList<String>()
}
