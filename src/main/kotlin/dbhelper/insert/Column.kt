package dbhelper.insert

data class Column(val name: String, val values: List<Any?>) {
    val size = values.size

    fun getOrNull(index: Int): Any? {
        return values.getOrNull(index)
    }

    companion object {
        @JvmStatic
        infix fun String.SET(values: List<Any?>): Column {
            return Column(this, values)
        }

        @JvmStatic
        infix fun String.SET(value: Any?): Column {
            return Column(this, listOf(value))
        }
    }
}
