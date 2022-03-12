package dbhelper.query.join

import dbhelper.query.conditions.Eq

class RightJoin(table: String, eq: Eq) : Join("RIGHT JOIN", table, eq) {
    companion object {
        @JvmStatic
        infix fun String.rightJoin(on: Eq): Join {
            return RightJoin(this, on)
        }
    }
}
