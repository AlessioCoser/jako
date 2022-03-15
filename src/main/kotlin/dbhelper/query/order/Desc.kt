package dbhelper.query.order

class Desc(vararg fields: String) : Order("DESC", *fields) {
    companion object {
        @JvmStatic
        fun DESC(vararg fields: String) = Desc(*fields)
    }
}
