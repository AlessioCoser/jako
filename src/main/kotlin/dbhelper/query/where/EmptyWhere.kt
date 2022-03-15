package dbhelper.query.where

class EmptyWhere: Where {
    override fun toString() = ""
    override fun params() = emptyList<String>()
}
