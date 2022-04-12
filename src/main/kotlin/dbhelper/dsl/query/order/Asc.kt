package dbhelper.dsl.query.order

class Asc(vararg fields: String) : OrderField("ASC", *fields) {
    companion object {
        @JvmStatic
        fun ASC(vararg fields: String) = Asc(*fields)
    }
}
