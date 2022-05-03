package jako.dsl.where

import jako.dsl.StatementBlock

interface Where: StatementBlock {
    override fun toString(): String
    override fun params(): List<Any?>
}
