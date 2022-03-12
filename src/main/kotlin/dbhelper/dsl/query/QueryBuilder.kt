package dbhelper.dsl.query

interface QueryBuilder {
    fun build(): Query
    fun single(): QueryBuilder
}
