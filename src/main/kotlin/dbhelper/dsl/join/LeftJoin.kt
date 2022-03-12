package dbhelper.dsl.join

import dbhelper.dsl.conditions.Condition

infix fun String.leftJoin(on: Condition): Join {
    return LeftJoin(this, on)
}

class LeftJoin(table: String, condition: Condition): Join(JoinType.LEFT_JOIN, table, condition)
