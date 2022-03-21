package dbhelper.insert

import dbhelper.query.fields.Fields.Companion.wrap


data class Insert(val statement: String, val params: List<Any?>) {
    constructor(
        into: String,
        values: Values
    ) : this("INSERT INTO ${into.wrap()}${values.columns()}${values.values()}", values.params())
}
