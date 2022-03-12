package dbhelper.query.join

class LeftJoin(table: String, field1: String, field2: String? = null) : Join("LEFT JOIN", table, field1) {
    companion object {
        @JvmStatic
        infix fun String.leftJoin(field1: String): Join {
            return Join("LEFT JOIN", this, field1)
        }
    }
}
