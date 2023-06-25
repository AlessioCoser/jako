package jako.dsl.query.join

import jako.dsl.Dialect

internal sealed class TypedJoin(private val type: String, private val on: On) : Join {
    override fun toSQL(dialect: Dialect) = "$type ${on.toSQL(dialect)}"
    override fun params() = on.params()
    override fun isPresent() = type.isNotBlank() && on.isPresent()
}

internal class InnerJoin(on: On) : TypedJoin("INNER JOIN", on)
internal class LeftJoin(on: On) : TypedJoin("LEFT JOIN", on)
internal class RightJoin(on: On) : TypedJoin("RIGHT JOIN", on)
