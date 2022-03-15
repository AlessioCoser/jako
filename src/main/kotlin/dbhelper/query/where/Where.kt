package dbhelper.query.where

interface Where {
    override fun toString(): String
    fun params(): List<Any?>
}
