package jako.dsl.fields

import jako.dsl.Dialect
import jako.dsl.StatementBlock

interface Field: StatementBlock {
    override fun toSQL(dialect: Dialect): String
    override fun params(): List<Any?>

    operator fun plus(i: Int): Field {
        return Raw("${toSQL()} + $i", params())
    }

    operator fun minus(i: Int): Field {
        return Raw("${toSQL()} - $i", params())
    }
}

val ALL: Field = Raw("*")
