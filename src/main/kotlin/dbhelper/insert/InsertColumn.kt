package dbhelper.insert

data class InsertColumn(val name: String, val value: Any?) {
    companion object {
        @JvmStatic
        infix fun String.SET(value: Any?): InsertColumn {
            return InsertColumn(this, value)
        }
    }
}
