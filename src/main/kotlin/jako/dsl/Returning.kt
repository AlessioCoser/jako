package jako.dsl

import jako.dsl.fields.Column
import jako.dsl.fields.Field


internal class Returning(private vararg val fields: Field): StatementBlock {
    constructor(): this(*emptyList<Field>().toTypedArray())
    constructor(vararg fields: String): this(*(fields.map { Column(it) }).toTypedArray())

    override fun toString(): String {
        if (fields.isEmpty()) {
            return ""
        }
        return " RETURNING ${fields.joinToString(separator = ", ") { it.toString() }}"
    }
    override fun params(): List<Any?> = fields.flatMap { it.params() }
}
