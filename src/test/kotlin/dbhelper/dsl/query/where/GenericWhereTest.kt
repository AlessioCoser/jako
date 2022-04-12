package dbhelper.dsl.query.where

import dbhelper.dsl.conditions.Eq.Companion.EQ
import dbhelper.dsl.where.GenericWhere
import dbhelper.dsl.where.NoWhere
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GenericWhereTest {
    @Test
    fun `empty where`() {
        val where = NoWhere()

        assertEquals("", where.toString())
        assertEquals(emptyList<Any?>(), where.params())
    }

    @Test
    fun `where statement`() {
        val where = GenericWhere("test" EQ "value")

        assertEquals(" WHERE \"test\" = ?", where.toString())
        assertEquals(listOf("value"), where.params())
    }
}