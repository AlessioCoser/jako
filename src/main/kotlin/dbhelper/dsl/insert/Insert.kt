package dbhelper.dsl.insert

import dbhelper.dsl.Returning
import dbhelper.dsl.Statement


data class Insert(override val statement: String, override val params: List<Any?>): Statement {
    constructor(
        into: Into,
        insertRow: InsertRow,
        returning: Returning
    ) : this("INSERT$into$insertRow$returning", insertRow.params() + returning.params())
}
