package dbhelper.session

import javax.sql.DataSource


interface SessionManager {
    val dataSource: DataSource
    fun <T> session(fn: Session.() -> T): T
}
