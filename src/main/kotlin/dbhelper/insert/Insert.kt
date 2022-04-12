package dbhelper.insert

import dbhelper.Statement
import dbhelper.query.fields.Fields.Companion.wrap


data class Insert(override val statement: String, override val params: List<Any?>): Statement {
    constructor(
        into: String,
        insertValues: InsertValues
    ) : this("INSERT INTO ${into.wrap()}${insertValues.statement()}", insertValues.params())
}
