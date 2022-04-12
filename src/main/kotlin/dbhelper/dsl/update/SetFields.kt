package dbhelper.dsl.update

class SetFields {
    private val cols: MutableList<SetColumn> = mutableListOf()

    fun add(column: SetColumn): SetFields {
        cols.add(column)
        return this
    }

    fun isNotEmpty() = cols.isNotEmpty()

    override fun toString() = " SET " + cols.joinToString(", ")

    fun params() = cols.map { it.value }
}
