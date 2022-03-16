package dbhelper.query.join

sealed class TypedJoin(private val type: String, private val on: On) : Join {
    override fun toString(): String {
        return "$type $on"
    }
}

class InnerJoin(on: On) : TypedJoin("INNER JOIN", on)
class LeftJoin(on: On) : TypedJoin("LEFT JOIN", on)
class RightJoin(on: On) : TypedJoin("RIGHT JOIN", on)