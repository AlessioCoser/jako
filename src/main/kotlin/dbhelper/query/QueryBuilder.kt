package dbhelper.query

interface QueryBuilder {
    fun build(): dbhelper.query.Query
    fun single(): dbhelper.query.QueryBuilder
}
