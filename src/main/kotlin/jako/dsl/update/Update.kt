package jako.dsl.update

import jako.dsl.StatementBuilder
import jako.dsl.conditions.Condition
import jako.dsl.where.GenericWhere
import jako.dsl.where.NoWhere
import jako.dsl.where.Where
import java.sql.Date
import java.time.LocalDate

class Update: StatementBuilder() {
    private var table: UpdateTable? = null
    private val fields: SetFields = SetFields()
    private var where: Where = NoWhere()

    override fun blocks() = listOf(updateTableOrThrow(), fieldsOrThrow(), where)

    fun table(table: String): Update {
        this.table = UpdateTable(table)
        return this
    }

    fun where(condition: Condition): Update {
        where = GenericWhere(condition)
        return this
    }

    fun set(column: String, value: Any?): Update {
        fields.add(SetField(column, value))
        return this
    }

    fun set(column: String, value: LocalDate?): Update {
        fields.add(SetField(column, if (value == null) null else Date.valueOf(value)))
        return this
    }

    private fun fieldsOrThrow() = if(fields.isNotEmpty()) fields else throw RuntimeException("Cannot generate update without values")

    private fun updateTableOrThrow() = table ?: throw RuntimeException("Cannot generate update without table name")

    companion object {
        fun table(name: String): Update {
            return Update().table(name)
        }
    }
}
