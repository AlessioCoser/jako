package dbhelper.query.join

import dbhelper.query.conditions.Eq

class InnerJoin(table: String, eq: Eq) : Join("INNER JOIN", table, eq) {
    companion object {
        @JvmStatic
        infix fun String.on(value: Eq): Join {
            return innerJoin(value)
        }

        @JvmStatic
        infix fun String.join(value: Eq): Join {
            return innerJoin(value)
        }

        @JvmStatic
        infix fun String.innerJoin(on: Eq): Join {
            return InnerJoin(this, on)
        }
    }
}
