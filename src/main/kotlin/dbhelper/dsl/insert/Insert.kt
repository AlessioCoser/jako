package dbhelper.dsl.insert

import dbhelper.dsl.Statement


data class Insert(override val statement: String, override val params: List<Any?>): Statement {
    constructor(
        into: Into,
        insertRow: InsertRow
    ) : this("INSERT$into$insertRow", insertRow.params())
}
