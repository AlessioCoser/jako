package jako.dsl.fields

import jako.dsl.StatementBlock

interface Field: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?>

    operator fun plus(i: Int): Field {
        return Raw("${toString()} + $i", params())
    }

    operator fun minus(i: Int): Field {
        return Raw("${toString()} - $i", params())
    }

    companion object {
        val ALL: Field = Raw("*")
    }
}