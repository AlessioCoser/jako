package dbhelper.dsl.where

interface Where {
    override fun toString(): String
    fun params(): List<Any?>
}
