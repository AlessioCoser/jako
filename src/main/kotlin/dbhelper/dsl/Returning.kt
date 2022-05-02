package dbhelper.dsl

import dbhelper.dsl.fields.Column
import dbhelper.dsl.fields.Field


class Returning(private vararg val fields: Field) {
    constructor(): this(*emptyList<Field>().toTypedArray())
    constructor(vararg fields: String): this(*(fields.map { Column(it) }).toTypedArray())

    override fun toString(): String {
        if (fields.isEmpty()) {
            return ""
        }
        return " RETURNING ${fields.joinToString(separator = ", ") { it.toString() }}"
    }
    fun params(): List<Any?> = fields.flatMap { it.params() }
}
