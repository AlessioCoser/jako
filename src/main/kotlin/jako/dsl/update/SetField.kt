package jako.dsl.update

import jako.dsl.Dialect
import jako.dsl.StatementBlock
import jako.dsl.fields.Column

internal data class SetField(private val name: String, private val param: Any?): StatementBlock {
    override fun toSQL(dialect: Dialect) = "${Column(name).toSQL(dialect)} = ?"
    override fun params(): List<Any?> = listOf(param)
    override fun isPresent() = name.isNotBlank()
}
