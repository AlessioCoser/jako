package dbhelper.dsl.fields

interface Field {
    override fun toString(): String

    operator fun plus(i: Int): Field {
        return Raw("${toString()} + $i")
    }

    operator fun minus(i: Int): Field {
        return Raw("${toString()} - $i")
    }

    companion object {
        val ALL: Field = Raw("*")
    }
}