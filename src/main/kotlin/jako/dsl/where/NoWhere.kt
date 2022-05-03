package jako.dsl.where

class NoWhere: Where {
    override fun toString() = ""
    override fun params() = emptyList<String>()
}
