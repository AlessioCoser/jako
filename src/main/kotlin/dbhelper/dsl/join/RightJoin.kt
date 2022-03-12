package dbhelper.dsl.join

import dbhelper.dsl.conditions.Condition

infix fun String.rightJoin(on: Condition): Join {
    return RightJoin(this, on)
}

class RightJoin(table: String, condition: Condition): Join(JoinType.RIGHT_JOIN, table, condition)
