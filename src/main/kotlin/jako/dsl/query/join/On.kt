package jako.dsl.query.join

import jako.dsl.fields.Column

class On(private val table: String, private val field1: String, private val field2: String? = null) {

    constructor(join: On, field2: String) : this(join.table, join.field1, field2)

    override fun toString(): String {
        if (field2 == null) {
            return "${Column(table)} USING(${Column(field1)})"
        }
        return "${Column(table)} ON ${Column(field1)} = ${Column(field2)}"
    }

    companion object {
        infix fun On.EQ(field2: String): On {
            return On(this, field2)
        }

        infix fun String.ON(field1: String): On {
            return On(this, field1)
        }
    }
}
