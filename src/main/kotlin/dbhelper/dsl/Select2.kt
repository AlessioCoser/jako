package dbhelper.dsl

class Select2 {
    private var from: String = ""
    private var fields: List<String> = listOf("*")

    fun from(table: String) {
        this.from = table
    }

    fun fields(vararg fields: String) {
        this.fields = fields.toList()
    }

    fun build(): String {
        return "SELECT ${joinFields()} FROM $from"
    }

    private fun joinFields(): String {
        return fields.joinToString(separator = ", ")
    }

    override fun toString() = "Select2<${build()}>"
}