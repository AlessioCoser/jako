package dbhelper.query.join

class RightJoin(table: String, field1: String) : Join("RIGHT JOIN", table, field1) {
    companion object {
        @JvmStatic
        infix fun String.rightJoin(field1: String): Join {
            return RightJoin(this, field1)
        }
    }
}
