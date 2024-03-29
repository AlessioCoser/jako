package jako.dsl.fields.functions

import jako.dsl.Dialect.All.PSQL
import jako.dsl.fields.col
import jako.dsl.fields.value
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MaxFieldTest {
    @Test
    fun `MAX field with string`() {
        val field = MAX("table.value")
        assertEquals("MAX(\"table\".\"value\")", field.toSQL(PSQL))
    }

    @Test
    fun `MAX field with column field`() {
        val field = MAX("column".col)
        assertEquals("MAX(\"column\")", field.toSQL(PSQL))
        assertEquals(emptyList<Any?>(), field.params())
    }

    @Test
    fun `MAX field with Value field`() {
        val field = MAX(1.value)
        assertEquals("MAX(?)", field.toSQL(PSQL))
        assertEquals(listOf(1), field.params())
    }
}