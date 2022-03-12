package dbhelper.query.join

class InnerJoin(table: String, field1: String, field2: String? = null) : Join("INNER JOIN", table, field1, field2) {
    companion object {
        @JvmStatic
        infix fun String.join(field1: String): Join {
            return innerJoin(field1)
        }

        @JvmStatic
        infix fun String.innerJoin(field1: String): Join {
            return Join("INNER JOIN", this, field1)
        }
    }
}
