package dbhelper.dsl.update

import dbhelper.dsl.StatementBuilder
import dbhelper.dsl.conditions.Condition
import dbhelper.dsl.fields.Fields.Companion.wrap
import dbhelper.dsl.where.GenericWhere
import dbhelper.dsl.where.NoWhere
import dbhelper.dsl.where.Where
import java.sql.Date
import java.time.LocalDate

class UpdateBuilder: StatementBuilder {
    private var rawUpdate: Update? = null
    private var table: String? = null
    private val fields: SetFields = SetFields()
    private var where: Where = NoWhere()

    fun raw(statement: String, vararg params: Any?): UpdateBuilder {
        rawUpdate = Update(statement, params.toList())
        return this
    }

    fun from(table: String): UpdateBuilder {
        this.table = table.wrap()
        return this
    }

    fun where(condition: Condition): UpdateBuilder {
        where = GenericWhere(condition)
        return this
    }

    fun set(column: String, value: Any?): UpdateBuilder {
        fields.add(SetColumn(column, value))
        return this
    }

    fun set(column: String, value: LocalDate?): UpdateBuilder {
        fields.add(SetColumn(column, if (value == null) null else Date.valueOf(value)))
        return this
    }

    override fun build(): Update {
        return rawUpdate ?: Update(tableOrThrow(), fieldsOrThrow(), where)
    }

    private fun fieldsOrThrow() = if(fields.isNotEmpty()) fields else throw RuntimeException("Cannot generate update without values")

    private fun tableOrThrow(): String = table ?: throw RuntimeException("Cannot generate update without table name")

    companion object {
        @JvmStatic
        fun raw(statement: String, vararg params: Any?) = UpdateBuilder().raw(statement, *params).build()
    }
}
