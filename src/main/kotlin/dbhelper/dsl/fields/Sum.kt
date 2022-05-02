package dbhelper.dsl.fields

class Sum(private val value: Field): Field {
    override fun toString() = "SUM($value)"

    companion object {
        @JvmStatic
        fun SUM(fieldName: String) = Sum(Column(fieldName))

        @JvmStatic
        fun SUM(fieldName: Field) = Sum(fieldName)
    }
}