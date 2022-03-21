package dbhelper.insert

data class Column(val name: String, val value: Any?) {
    companion object {
        @JvmStatic
        infix fun String.SET(value: Any?): Column {
            return Column(this, value)
        }
    }
}
