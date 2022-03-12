package dbhelper.query.join

class On(private val table: String, private val field1: String, private val field2: String? = null) {

    constructor(join: On, field2: String) : this(join.table, join.field1, field2)

    override fun toString(): String {
        if (field2 == null) {
            return "$table USING($field1)"
        }
        return "$table ON $field1 = $field2"
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

