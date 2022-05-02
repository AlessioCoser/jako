package dbhelper.dsl.fields


class Fields(private vararg val fields: Field) {

    constructor(vararg fields: String): this(*(fields.map { Column(it) }).toTypedArray())

    override fun toString() = fields.joinToString(separator = ", ") { it.toString() }
}
