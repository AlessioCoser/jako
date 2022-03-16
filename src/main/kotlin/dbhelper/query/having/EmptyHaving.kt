package dbhelper.query.having

class EmptyHaving: Having {
    override fun toString() = ""
    override fun params() = emptyList<String>()
}
