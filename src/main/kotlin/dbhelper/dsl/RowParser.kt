package dbhelper.dsl

interface RowParser<T> {
    fun parse(row: Row): T
}
