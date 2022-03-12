package dbhelper.query

interface SessionManager {
    fun <T> session(fn: SessionSql.() -> T): T
}