package jako.dsl.query.limit

internal class LimitTo(private val limit: Int, private val offset: Int = 0) : Limit {
    override fun toString() = if (offset != 0) {
        "LIMIT $limit OFFSET $offset"
    } else {
        "LIMIT $limit"
    }
}
