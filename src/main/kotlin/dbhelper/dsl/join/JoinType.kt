package dbhelper.dsl.join

enum class JoinType(val value: String) {
    INNER_JOIN("INNER JOIN"),
    LEFT_JOIN("LEFT JOIN"),
    RIGHT_JOIN("RIGHT JOIN")
}
