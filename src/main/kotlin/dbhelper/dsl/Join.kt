package dbhelper.dsl

import dbhelper.dsl.JoinType.*
import dbhelper.dsl.conditions.Condition

infix fun String.on(value: Condition): Join {
    return innerJoin(value)
}

infix fun String.innerJoin(on: Condition): Join {
    return InnerJoin(this, on)
}

infix fun String.leftJoin(on: Condition): Join {
    return LeftJoin(this, on)
}

infix fun String.rightJoin(on: Condition): Join {
    return RightJoin(this, on)
}

class InnerJoin(table: String, condition: Condition): Join(INNER_JOIN, table, condition)
class LeftJoin(table: String, condition: Condition): Join(LEFT_JOIN, table, condition)
class RightJoin(table: String, condition: Condition): Join(RIGHT_JOIN, table, condition)

abstract class Join (private val type: JoinType, private val table: String, private val condition: Condition) {

    fun statement(): String {
        return "${type.value} $table ON ${compileCondition(condition)}"
    }

    private fun compileCondition(condition: Condition): String {
        val statement = condition.statement()
        val params = condition.params()
        return params.fold(statement) { acc, value -> acc.replaceFirst("?", value.toString()) }
    }
}

enum class JoinType(val value: String) {
    INNER_JOIN("INNER JOIN"),
    LEFT_JOIN("LEFT JOIN"),
    RIGHT_JOIN("RIGHT JOIN")
}