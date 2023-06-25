package jako.dsl.fields

import jako.dsl.Dialect

class As(private val column: Field, private val name: Field): Field {
    override fun toSQL(dialect: Dialect) = "${column.toSQL(dialect)} AS ${name.toSQL(dialect)}"
    override fun params() = column.params() + name.params()
    override fun isPresent() = column.isPresent() && name.isPresent()
}

infix fun String.AS(name: String): Field {
    return As(Column(this), Column(name))
}

infix fun Field.AS(name: String): Field {
    return As(this, Column(name))
}
