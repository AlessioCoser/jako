package jako.dsl.fields

import jako.dsl.Dialect
import jako.dsl.StatementBlock
import jako.dsl.fields.functions.Minus
import jako.dsl.fields.functions.Plus

interface Field: StatementBlock {
    override fun toSQL(dialect: Dialect): String
    override fun params(): List<Any?>

    operator fun plus(i: Int): Field {
        return Plus(this, i)
    }

    operator fun minus(i: Int): Field {
        return Minus(this, i)
    }
}

val ALL: Field = Raw("*")
