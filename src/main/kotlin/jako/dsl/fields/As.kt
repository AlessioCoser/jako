package jako.dsl.fields

class As(private val column: Field, private val name: Field): Field {
    override fun toString() = "$column AS $name"
    override fun params() = column.params() + name.params()
}

infix fun String.AS(name: String): Field {
    return As(Column(this), Column(name))
}

infix fun Field.AS(name: String): Field {
    return As(this, Column(name))
}
