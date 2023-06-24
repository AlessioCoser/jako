package jako.dsl.where

import jako.dsl.Dialect
import jako.dsl.StatementBlock

internal interface Where: StatementBlock {
    override fun toSQL(dialect: Dialect): String
    override fun params(): List<Any?>
}
