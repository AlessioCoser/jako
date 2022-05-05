package jako.dsl.query.order

class Asc(vararg fields: String) : OrderField("ASC", *fields)

fun ASC(vararg fields: String) = Asc(*fields)
