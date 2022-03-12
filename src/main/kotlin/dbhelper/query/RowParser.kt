package dbhelper.query

interface RowParser<T> {
    fun parse(row: Row): T
}
