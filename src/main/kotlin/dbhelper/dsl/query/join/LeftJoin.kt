package dbhelper.dsl.query.join

import dbhelper.dsl.query.conditions.Condition

class LeftJoin(table: String, condition: Condition) : Join("LEFT JOIN", table, condition) {
    companion object {
        @JvmStatic
        infix fun String.leftJoin(on: Condition): Join {
            return LeftJoin(this, on)
        }
    }
}
