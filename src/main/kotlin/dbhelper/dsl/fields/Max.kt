package dbhelper.dsl.fields

class Max(private val value: Field): Field {
    override fun toString() = "MAX($value)"

    companion object {
        @JvmStatic
        fun MAX(fieldName: String) = Max(Column(fieldName))

        @JvmStatic
        fun MAX(field: Field) = Max(field)
    }
}