package dbhelper.dsl.insert

import dbhelper.dsl.Returning
import dbhelper.dsl.StatementBuilder
import dbhelper.dsl.fields.Field
import java.sql.Date
import java.time.LocalDate

class Insert: StatementBuilder() {
    private var insertInto: InsertInto? = null
    private var insertRow: InsertRow = InsertRow()
    private var returning: Returning = Returning()

    override fun blocks() = listOf(insertIntoOrThrow(), rowOrThrow(), returning)

    fun into(table: String): Insert {
        this.insertInto = InsertInto(table)
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

    private fun rowOrThrow() = if (insertRow.isNotEmpty()) insertRow else throw RuntimeException("Cannot generate insert without values")
    private fun insertIntoOrThrow() = insertInto ?: throw RuntimeException("Cannot generate insert without table name")
}
