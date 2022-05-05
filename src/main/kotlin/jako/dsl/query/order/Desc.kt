package jako.dsl.query.order

class Desc(vararg fields: String) : OrderField("DESC", *fields)

fun DESC(vararg fields: String) = Desc(*fields)
