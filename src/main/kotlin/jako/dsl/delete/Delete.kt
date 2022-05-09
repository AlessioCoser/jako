package jako.dsl.delete

import jako.dsl.Returning
import jako.dsl.StatementBuilder
import jako.dsl.conditions.Condition
import jako.dsl.fields.Field
import jako.dsl.fields.Raw
import jako.dsl.query.From
import jako.dsl.where.GenericWhere
import jako.dsl.where.NoWhere
import jako.dsl.where.Where

class Delete: StatementBuilder() {
    private var from: From? = null
    private var where: Where = NoWhere()
    private var returning: Returning = Returning()

    override fun blocks() = listOf(Raw("DELETE"), fromOrThrow(), where, returning)

    fun from(table: String): Delete {
        this.from = From(table)
        return this
    }

    fun where(condition: Condition): Delete {
        where = GenericWhere(condition)
        return this
    }

    fun returning(vararg fields: String): Delete {
        returning = Returning(*fields)
        return this
    }

    fun returning(vararg fields: Field): Delete {
        returning = Returning(*fields)
        return this
    }

    private fun fromOrThrow(): From = from ?: throw RuntimeException("Cannot generate delete without table name")

    companion object {
        fun from(name: String): Delete {
            return Delete().from(name)
        }
    }
}
