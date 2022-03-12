package dbhelper.dsl.query.order

class Desc(vararg fields: String) : Order(*fields) {
    override fun direction() = "DESC"

    companion object {
        @JvmStatic
        fun desc(vararg fields: String) = Desc(*fields)
    }
}
