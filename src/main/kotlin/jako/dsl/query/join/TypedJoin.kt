package jako.dsl.query.join

internal sealed class TypedJoin(private val type: String, private val on: On) : Join {
    override fun toString(): String {
        return "$type $on"
    }
}

internal class InnerJoin(on: On) : TypedJoin("INNER JOIN", on)
internal class LeftJoin(on: On) : TypedJoin("LEFT JOIN", on)
internal class RightJoin(on: On) : TypedJoin("RIGHT JOIN", on)