package dbhelper.dsl

interface QueryRowParser<T> {
    fun parse(row: Row): T
}
