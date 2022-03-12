package dbhelper.dsl

interface SessionManager {
    fun <T> session(fn: SqlSession.() -> T): T
}