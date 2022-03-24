package dbhelper

import dbhelper.insert.InsertBuilder
import dbhelper.query.QueryBuilder
import dbhelper.session.SessionManager
import dbhelper.session.SimpleSessionManager

class Database(private val manager: SessionManager) {
    constructor(jdbc: String, user: String, password: String): this(SimpleSessionManager(jdbc, user, password))
    val db: org.ktorm.database.Database
        get() = org.ktorm.database.Database.connect(manager.dataSource)

    fun insert(prepare: InsertBuilder.() -> Unit) {
        val insertBuilder = InsertBuilder()
        prepare(insertBuilder)
        insert(insertBuilder)
    }

    fun insert(insertBuilder: InsertBuilder) {
        manager.session { execute(insertBuilder.build()) }
    }

    fun select(prepare: QueryBuilder.() -> Unit): QueryExecutor {
        val queryBuilder = QueryBuilder()
        prepare(queryBuilder)
        return select(queryBuilder)
    }

    fun select(queryBuilder: QueryBuilder): QueryExecutor {
        return QueryExecutor(manager, queryBuilder)
    }
}
