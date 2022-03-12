package dbhelper.dsl.query.order

class Asc(vararg fields: String) : Order(*fields) {
    override fun direction() = "ASC"

    companion object {
        @JvmStatic
        fun asc(vararg fields: String) = Asc(*fields)
    }
}
