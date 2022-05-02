package dbhelper.dsl.fields

class Count(private val value: Field): Field {
    override fun toString() = "COUNT($value)"

    companion object {
        @JvmStatic
        fun COUNT(fieldName: String) = Count(Column(fieldName))

        @JvmStatic
        fun COUNT(field: Field) = Count(field)
    }
}