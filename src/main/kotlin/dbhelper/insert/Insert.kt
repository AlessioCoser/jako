package dbhelper.insert

import dbhelper.Statement
import dbhelper.query.fields.Fields.Companion.wrap


data class Insert(override val statement: String, override val params: List<Any?>): Statement {
    constructor(
        into: String,
        insertRow: InsertRow
    ) : this("INSERT INTO ${into.wrap()} ${insertRow.statement()}", insertRow.params())
}
