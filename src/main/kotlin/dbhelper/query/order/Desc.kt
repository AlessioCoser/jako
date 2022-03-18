package dbhelper.query.order

class Desc(vararg fields: String) : OrderField("DESC", *fields) {
    companion object {
        @JvmStatic
        fun DESC(vararg fields: String) = Desc(*fields)
    }
}
