package dbhelper.dsl.order

fun asc(vararg fields: String) = Asc(*fields)

class Asc(vararg fields: String): Order(*fields) {
    override fun direction() = "ASC"
}