package dbhelper.dsl

interface WhereCondition {
    fun statement(): String
}

class Empty: WhereCondition {
    override fun statement() = "true"
}

class Eq(private val left: String, private val right: String): WhereCondition {
    override fun statement(): String {
        return "$left = $right"
    }
}

class Gt(private val left: String, private val right: String): WhereCondition {
    override fun statement(): String {
        return "$left > $right"
    }
}

class And(private val left: WhereCondition, private val right: WhereCondition): WhereCondition {
    override fun statement(): String {
        return "(${left.statement()} AND ${right.statement()})"
    }
}

class Or(private val left: WhereCondition, private val right: WhereCondition): WhereCondition {
    override fun statement(): String {
        return "(${left.statement()} OR ${right.statement()})"
    }
}

infix fun String.eq(value: String): WhereCondition {
    return Eq(this, "'$value'")
}

infix fun String.eq(value: Int): WhereCondition {
    return Eq(this, value.toString())
}

infix fun String.gt(value: Int): WhereCondition {
    return Gt(this, value.toString())
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

    private fun joinFields(): String {
        return fields.joinToString(separator = ", ")
    }

    override fun toString() = "Select2<${build()}>"
}