package dbhelper.dsl.fields

class Every(private val value: Field): Field {
    override fun toString() = "EVERY($value)"

    companion object {
        @JvmStatic
        fun EVERY(fieldName: String) = Every(Column(fieldName))

        @JvmStatic
        fun EVERY(fieldName: Field) = Every(fieldName)
    }
}