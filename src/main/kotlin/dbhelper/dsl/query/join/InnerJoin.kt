package dbhelper.dsl.query.join

import dbhelper.dsl.query.conditions.Condition

class InnerJoin(table: String, condition: Condition) : Join("INNER JOIN", table, condition) {
    companion object {
        @JvmStatic
        infix fun String.on(value: Condition): Join {
            return innerJoin(value)
        }

        @JvmStatic
        infix fun String.innerJoin(on: Condition): Join {
            return InnerJoin(this, on)
        }
    }
}
