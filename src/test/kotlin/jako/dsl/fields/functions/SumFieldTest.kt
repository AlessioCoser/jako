package jako.dsl.fields.functions

import jako.dsl.Dialect.All.PSQL
import jako.dsl.fields.col
import jako.dsl.fields.value
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SumFieldTest {
    @Test
    fun `SUM field with string`() {
        val field = SUM("table.value")
        assertEquals("SUM(\"table\".\"value\")", field.toSQL(PSQL))
    }

    @Test
    fun `SUM field with column field`() {
        val field = SUM("column".col)
        assertEquals("SUM(\"column\")", field.toSQL(PSQL))
        assertEquals(emptyList<Any?>(), field.params())
    }

    @Test
    fun `SUM field with Value field`() {
        val field = SUM(1.value)
        assertEquals("SUM(?)", field.toSQL(PSQL))
        assertEquals(listOf(1), field.params())
    }
}