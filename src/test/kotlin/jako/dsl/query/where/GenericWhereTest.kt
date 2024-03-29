package jako.dsl.query.where

import jako.dsl.Dialect.All.PSQL
import jako.dsl.conditions.EQ
import jako.dsl.where.GenericWhere
import jako.dsl.where.NoWhere
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GenericWhereTest {
    @Test
    fun `empty where`() {
        val where = NoWhere()

        assertEquals("", where.toSQL(PSQL))
        assertEquals(emptyList<Any?>(), where.params())
    }

    @Test
    fun `where statement`() {
        val where = GenericWhere("test" EQ "value")

        assertEquals("WHERE \"test\" = ?", where.toSQL(PSQL))
        assertEquals(listOf("value"), where.params())
    }
}