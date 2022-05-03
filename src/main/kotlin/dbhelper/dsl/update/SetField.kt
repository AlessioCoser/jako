package dbhelper.dsl.update

import dbhelper.dsl.StatementBlock
import dbhelper.dsl.fields.Column

data class SetField(private val name: String, private val param: Any?): StatementBlock {
    override fun toString() = "${Column(name)} = ?"
    override fun params(): List<Any?> = listOf(param)
}
