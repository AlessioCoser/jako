package dbhelper.insert

class InsertBuilder {
    private var rawInsert: Insert? = null
    private var into: String? = null
    private var values: Values = Values()

    fun raw(statement: String, vararg params: Any?): InsertBuilder {
        rawInsert = Insert(statement, params.toList())
        return this
    }

    fun into(table: String): InsertBuilder {
        this.into = table
        return this
    }

    fun values(vararg values: Column): InsertBuilder {
        this.values.add(Row(values.toList()))
        return this
    }

    fun build(): Insert {
        return rawInsert ?: Insert(intoOrThrow(), values)
    }

    private fun intoOrThrow(): String = into ?: throw RuntimeException("Cannot generate insert without table name")

    companion object {
        @JvmStatic
        fun raw(statement: String, vararg params: Any?) = InsertBuilder().raw(statement, *params).build()
    }
}
