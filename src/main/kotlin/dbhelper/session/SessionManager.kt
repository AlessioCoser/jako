package dbhelper.session

interface SessionManager {
    fun <T> session(fn: Session.() -> T): T
}
