package dbhelper.query.join

import dbhelper.query.conditions.Eq

class LeftJoin(table: String, eq: Eq) : Join("LEFT JOIN", table, eq) {
    companion object {
        @JvmStatic
        infix fun String.leftJoin(on: Eq): Join {
            return LeftJoin(this, on)
        }
    }
}
