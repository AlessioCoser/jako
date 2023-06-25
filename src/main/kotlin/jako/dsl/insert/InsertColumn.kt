package jako.dsl.insert

internal data class InsertColumn(val name: String, val value: Any?) {
    fun isPresent() = name.isNotBlank()
}
