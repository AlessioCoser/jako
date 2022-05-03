package dbhelper.dsl.where

import dbhelper.dsl.StatementBlock

interface Where: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?>
}
