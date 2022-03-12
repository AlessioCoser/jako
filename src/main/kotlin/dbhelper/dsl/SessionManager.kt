package dbhelper.dsl

interface SessionManager {
    fun <T> session(fn: SessionSql.() -> T): T
}