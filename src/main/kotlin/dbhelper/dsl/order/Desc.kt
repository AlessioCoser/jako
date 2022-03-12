package dbhelper.dsl.order

fun desc(vararg fields: String) = Desc(*fields)

class Desc(vararg fields: String): Order(*fields) {
    override fun direction() = "DESC"
}
