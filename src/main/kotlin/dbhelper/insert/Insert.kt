package dbhelper.insert

import dbhelper.Statement
import dbhelper.query.fields.Fields.Companion.wrap


data class Insert(override val statement: String, override val params: List<Any?>): Statement {
    constructor(
        into:
        String, values: Values
    ) : this("INSERT INTO ${into.wrap()}${values.columns()}${values.values()}", values.params())
}
