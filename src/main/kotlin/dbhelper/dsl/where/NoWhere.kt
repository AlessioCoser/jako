package dbhelper.dsl.where

class NoWhere: Where {
    override fun toString() = ""
    override fun params() = emptyList<String>()
}
