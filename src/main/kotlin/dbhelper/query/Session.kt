package dbhelper.query

interface Session {
    fun <T> execute(query: dbhelper.query.Query, parseRow: Row.() -> T): List<T>
}