package dbhelper.dsl.fields

class Avg(private val value: Field): Field {
    override fun toString() = "AVG($value)"

    companion object {
        @JvmStatic
        fun AVG(fieldName: String) = Avg(Column(fieldName))

        @JvmStatic
        fun AVG(field: Field) = Avg(field)
    }
}