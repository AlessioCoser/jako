package dbhelper.dsl.fields

class Coalesce(private val value: Field, private val default: Field): Field {
    override fun toString() = "COALESCE($value, $default)"

    companion object {
        @JvmStatic
        fun COALESCE(fieldName: String, default: Any) = Coalesce(Column(fieldName), Raw(default))

        @JvmStatic
        fun COALESCE(fieldName: Field, default: Any) = Coalesce(fieldName, Raw(default))
    }
}