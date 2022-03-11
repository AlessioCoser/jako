package dbhelper.dsl

interface WhereCondition {
    fun statement(): String
    fun params(): List<Any?>
}

class Empty: WhereCondition {
    override fun statement() = "true"
    override fun params() = listOf<Any?>()
}

class Eq(private val left: String, private val right: Any?): WhereCondition {
    override fun statement(): String {
        return "$left = ?"
    }

    override fun params() = listOf(right)
}

class Gt(private val left: String, private val right: Any?): WhereCondition {
    override fun statement(): String {
        return "$left > ?"
    }

    override fun params() = listOf(right)
}

class And(private val left: WhereCondition, private val right: WhereCondition): WhereCondition {
    override fun statement(): String {
        return "(${left.statement()} AND ${right.statement()})"
    }

    override fun params() = left.params().plus(right.params())
}

class Or(private val left: WhereCondition, private val right: WhereCondition): WhereCondition {
    override fun statement(): String {
        return "(${left.statement()} OR ${right.statement()})"
    }

    override fun params() = left.params().plus(right.params())
}

infix fun String.eq(value: String): WhereCondition {
    return Eq(this, value)
}

infix fun String.eq(value: Int): WhereCondition {
    return Eq(this, value)
}

infix fun String.gt(value: Int): WhereCondition {
    return Gt(this, value)
}

infix fun WhereCondition.and(value: WhereCondition): WhereCondition {
    return And(this, value)
}

infix fun WhereCondition.or(value: WhereCondition): WhereCondition {
    return Or(this, value)
}

class Select2 {
    private var from: String = ""
    private var fields: List<String> = listOf("*")
    private var where: WhereCondition = Empty()

    fun from(table: String) {
        this.from = table
    }

    fun fields(vararg fields: String) {
        this.fields = fields.toList()
    }

    fun where(condition: WhereCondition) {
        where = condition
    }

    fun build(): String {
        return "SELECT ${joinFields()} FROM $from WHERE ${where.statement()}"
    }

    fun params(): List<Any?> {
        return where.params()
    }

    private fun joinFields(): String {
        return fields.joinToString(separator = ", ")
    }

    override fun toString() = "Select2<build=${build()}, params=${params()}>"
}