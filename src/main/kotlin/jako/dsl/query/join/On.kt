package jako.dsl.query.join

import jako.dsl.Dialect
import jako.dsl.StatementBlock
import jako.dsl.fields.Column
import jako.dsl.fields.Field

class On(private val table: Field, private val field1: String, private val field2: String? = null): StatementBlock {
    constructor(table: String, field1: String, field2: String? = null) : this(Column(table), field1, field2)
    constructor(join: On, field2: String) : this(join.table, join.field1, field2)

    override fun toSQL(dialect: Dialect): String {
        if (field2 == null) {
            return "${table.toSQL(dialect)} USING(${Column(field1).toSQL(dialect)})"
        }
        return "${table.toSQL(dialect)} ON ${Column(field1).toSQL(dialect)} = ${Column(field2).toSQL(dialect)}"
    }

    override fun params(): List<Any?> {
        return table.params()
    }

    override fun isPresent() = table.isPresent() && field1.isNotBlank() && field2?.isNotBlank() ?: false
}

infix fun On.EQ(field2: String): On {
    return On(this, field2)
}

infix fun String.ON(field1: String): On {
    return On(this, field1)
}


infix fun Field.ON(field1: String): On {
    return On(this, field1)
}

