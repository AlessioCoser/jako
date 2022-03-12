package dbhelper.dsl.join

import dbhelper.dsl.conditions.Condition

class RightJoin(table: String, condition: Condition) : Join("RIGHT JOIN", table, condition) {
    companion object {
        @JvmStatic
        infix fun String.rightJoin(on: Condition): Join {
            return RightJoin(this, on)
        }
    }
}
