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
        infix fun On.EQ(field2: String): On {
            return On(this, field2)
        }

        infix fun String.ON(field1: String): On {
            return On(this, field1)
        }
    }
}

interface Field {
    override fun toString(): String
}

class NormalField(private val field: String): Field {
    override fun toString() = field.wrap()
}

abstract class Aggregate(private val operator: String, private val field: String): Field {
    override fun toString(): String {
        return "$operator(${field.wrap()})"
    }
}

class Count(field: String): Aggregate("count", field) {
    companion object {
        infix fun COUNT(field: String) = Count(field)
    }
}
infix fun String.AS(name: String): String {
    return "${this.wrap()} AS ${name.wrap()}"
}

infix fun Aggregate.AS(name: String): String {
    return "$this AS ${name.wrap()}"
}
