package dbhelper.query.order

class Asc(vararg fields: String) : Order("ASC", *fields) {
    companion object {
        @JvmStatic
        fun ASC(vararg fields: String) = Asc(*fields)
    }
}
