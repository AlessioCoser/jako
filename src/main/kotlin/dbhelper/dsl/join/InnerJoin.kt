package dbhelper.dsl.join

import dbhelper.dsl.conditions.Condition

infix fun String.on(value: Condition): Join {
    return innerJoin(value)
}

infix fun String.innerJoin(on: Condition): Join {
    return InnerJoin(this, on)
}

class InnerJoin(table: String, condition: Condition): Join(JoinType.INNER_JOIN, table, condition)
