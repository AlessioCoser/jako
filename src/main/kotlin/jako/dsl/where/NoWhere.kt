package jako.dsl.where

internal class NoWhere: Where {
    override fun toString() = ""
    override fun params() = emptyList<String>()
}
