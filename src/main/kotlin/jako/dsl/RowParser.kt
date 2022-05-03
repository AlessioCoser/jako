package jako.dsl

interface RowParser<T> {
    fun parse(row: Row): T
}
