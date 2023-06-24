package jako.dsl.query.order

import jako.dsl.Dialect
import jako.dsl.StatementBlock

interface Order: StatementBlock {
    override fun toSQL(dialect: Dialect): String
    override fun params(): List<Any?> = emptyList()
}

