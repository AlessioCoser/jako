package dbhelper.dsl.query

interface RowParser<T> {
    fun parse(row: Row): T
}
