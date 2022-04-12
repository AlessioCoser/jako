package dbhelper.dsl.insert

import java.sql.Date
import java.time.LocalDate

class InsertBuilder {
    private var rawInsert: Insert? = null
    private var into: Into? = null
    private var insertRow: InsertRow = InsertRow()

    fun raw(statement: String, vararg params: Any?): InsertBuilder {
        rawInsert = Insert(statement, params.toList())
        return this
    }

    fun into(table: String): InsertBuilder {
        this.into = Into(table)
        return this
    }

    fun set(column: String, value: Any?): InsertBuilder {
        insertRow.add(InsertColumn(column, value))
        return this
    }

    fun set(column: String, value: LocalDate?): InsertBuilder {
        insertRow.add(InsertColumn(column, if (value == null) null else Date.valueOf(value)))
        return this
    }

    fun build(): Insert {
        return rawInsert ?: Insert(intoOrThrow(), rowOrThrow())
    }

    private fun rowOrThrow() = if (insertRow.isNotEmpty()) insertRow else throw RuntimeException("Cannot generate insert without values")

    private fun intoOrThrow() = into ?: throw RuntimeException("Cannot generate insert without table name")

    companion object {
        @JvmStatic
        fun raw(statement: String, vararg params: Any?) = InsertBuilder().raw(statement, *params).build()
    }
}
