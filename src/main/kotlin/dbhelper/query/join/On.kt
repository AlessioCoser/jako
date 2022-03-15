package dbhelper.query.join

import dbhelper.query.Wrap.wrap

class On(private val table: String, private val field1: String, private val field2: String? = null) {

    constructor(join: On, field2: String) : this(join.table, join.field1, field2)

    override fun toString(): String {
        if (field2 == null) {
            return "${table.wrap()} USING(${field1.wrap()})"
        }
        return "${table.wrap()} ON ${field1.wrap()} = ${field2.wrap()}"
    }

    companion object {
        infix fun On.eq(field2: String): On {
            return On(this, field2)
        }

        infix fun String.on(field1: String): On {
            return On(this, field1)
        }
    }
}
