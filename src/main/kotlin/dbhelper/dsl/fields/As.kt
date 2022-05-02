package dbhelper.dsl.fields

class As(private val column: Field, private val name: Field): Field {
    override fun toString() = "$column AS $name"
    override fun params() = column.params() + name.params()

    companion object {
        @JvmStatic
        infix fun String.AS(name: String): Field {
            return As(Column(this), Column(name))
        }

        @JvmStatic
        infix fun Field.AS(name: String): Field {
            return As(this, Column(name))
        }
    }
}