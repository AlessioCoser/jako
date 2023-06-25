package jako.dsl.fields.functions

import jako.dsl.Dialect.All.PSQL
import jako.dsl.fields.col
import jako.dsl.fields.value
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EveryFieldTest {
    @Test
    fun `EVERY field with string`() {
        val field = EVERY("table.value")
        assertEquals("EVERY(\"table\".\"value\")", field.toSQL(PSQL))
        assertEquals(emptyList<Any?>(), field.params())
    }

    @Test
    fun `EVERY field with column field`() {
        val field = EVERY("column".col)
        assertEquals("EVERY(\"column\")", field.toSQL(PSQL))
        assertEquals(emptyList<Any?>(), field.params())
    }

    @Test
    fun `EVERY field with Value field`() {
        val field = EVERY(1.value)
        assertEquals("EVERY(?)", field.toSQL(PSQL))
        assertEquals(listOf(1), field.params())
    }
}