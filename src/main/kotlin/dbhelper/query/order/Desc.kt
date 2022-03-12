package dbhelper.query.order

class Desc(vararg fields: String) : Order("DESC", *fields) {
    companion object {
        @JvmStatic
        fun desc(vararg fields: String) = Desc(*fields)
    }
}
