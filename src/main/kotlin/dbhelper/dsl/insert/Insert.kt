package dbhelper.dsl.insert

import dbhelper.dsl.Returning
import dbhelper.dsl.Statement
import dbhelper.dsl.StatementBuilder
import dbhelper.dsl.fields.Field
import java.sql.Date
import java.time.LocalDate

class Insert: StatementBuilder {
    private var rawInsert: Statement? = null
    private var into: Into? = null
    private var insertRow: InsertRow = InsertRow()
    private var returning: Returning = Returning()

    fun raw(statement: String, vararg params: Any?): Insert {
        rawInsert = Statement(statement, params.toList())
        return this
    }

    fun into(table: String): Insert {
        this.into = Into(table)
        return this
    }

    fun set(column: String, value: Any?): Insert {
        insertRow.add(InsertColumn(column, value))
        return this
    }

    fun set(column: String, value: LocalDate?): Insert {
        insertRow.add(InsertColumn(column, if (value == null) null else Date.valueOf(value)))
        return this
    }

    fun returning(vararg fields: String): Insert {
        returning = Returning(*fields)
        return this
    }

    fun returning(vararg fields: Field): Insert {
        returning = Returning(*fields)
        return this
    }

    override fun build(): Statement {
        return rawInsert ?: Statement("INSERT${intoOrThrow()}${rowOrThrow()}$returning", insertRow.params() + returning.params())
    }

    private fun rowOrThrow() = if (insertRow.isNotEmpty()) insertRow else throw RuntimeException("Cannot generate insert without values")
    private fun intoOrThrow() = into ?: throw RuntimeException("Cannot generate insert without table name")

    companion object {
        @JvmStatic
        fun raw(statement: String, vararg params: Any?) = Insert().raw(statement, *params).build()
    }
}
