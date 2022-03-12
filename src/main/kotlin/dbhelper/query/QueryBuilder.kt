package dbhelper.query

interface QueryBuilder {
    fun build(): Query
    fun single(): QueryBuilder
}
