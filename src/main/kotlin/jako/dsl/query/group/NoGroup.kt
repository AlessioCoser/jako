package jako.dsl.query.group

import jako.dsl.Dialect

internal class NoGroup: Group {
    override fun toSQL(dialect: Dialect) = ""
}
