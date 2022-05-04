package jako.dsl.fields


internal class Fields(private vararg val fields: Field): Field {
    constructor(vararg fields: String): this(*(fields.map { Column(it) }).toTypedArray())

    override fun toString() = fields.joinToString(separator = ", ") { it.toString() }
    override fun params(): List<Any?> = fields.flatMap { it.params() }
}
