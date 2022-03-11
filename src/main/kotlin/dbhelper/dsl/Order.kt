package dbhelper.dsl

interface Order {
    fun statement(): String
    fun direction(): String
    companion object {
        fun asc(vararg fields: String) = Asc(*fields)
        fun desc(vararg fields: String) = Desc(*fields)
    }
}

class Asc(private vararg val fields: String): Order {
    override fun statement() = fields.joinToString(", ")
    override fun direction() = "ASC"
}

class Desc(private vararg val fields: String): Order {
    override fun statement() = fields.joinToString(", ")
    override fun direction() = "DESC"
}
