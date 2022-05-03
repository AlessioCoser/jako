package dbhelper.dsl.update

import dbhelper.dsl.Statement
import dbhelper.dsl.conditions.Condition
import dbhelper.dsl.fields.Column
import dbhelper.dsl.where.GenericWhere
import dbhelper.dsl.where.NoWhere
import dbhelper.dsl.where.Where
import java.sql.Date
import java.time.LocalDate

class Update: Statement {
    private var table: Column? = null
    private val fields: SetFields = SetFields()
    private var where: Where = NoWhere()

    override fun toString() = "UPDATE ${tableOrThrow()}${fieldsOrThrow()}$where"
    override fun params(): List<Any?> = fields.params() + where.params()

    fun from(table: String): Update {
        this.table = Column(table)
        return this
    }

    fun where(condition: Condition): Update {
        where = GenericWhere(condition)
        return this
    }

    fun set(column: String, value: Any?): Update {
        fields.add(SetColumn(column, value))
        return this
    }

    fun set(column: String, value: LocalDate?): Update {
        fields.add(SetColumn(column, if (value == null) null else Date.valueOf(value)))
        return this
    }

    private fun fieldsOrThrow() = if(fields.isNotEmpty()) fields else throw RuntimeException("Cannot generate update without values")

    private fun tableOrThrow(): Column = table ?: throw RuntimeException("Cannot generate update without table name")
}
