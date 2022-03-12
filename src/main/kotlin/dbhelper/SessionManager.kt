package dbhelper

interface SessionManager {
    fun <T> session(fn: SessionSql.() -> T): T
}