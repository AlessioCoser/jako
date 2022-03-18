package dbhelper.query.where

class NoWhere: Where {
    override fun toString() = ""
    override fun params() = emptyList<String>()
}
