package jako.dsl.fields.functions

import jako.dsl.Dialect.All.PSQL
import jako.dsl.fields.col
import jako.dsl.fields.value
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MinFieldTest {
    @Test
    fun `MIN field with string`() {
        val field = MIN("table.value")
        assertEquals("MIN(\"table\".\"value\")", field.toSQL(PSQL))
    }

    @Test
    fun `MIN field with column field`() {
        val field = MIN("column".col)
        assertEquals("MIN(\"column\")", field.toSQL(PSQL))
        assertEquals(emptyList<Any?>(), field.params())
    }

    @Test
    fun `MIN field with Value field`() {
        val field = MIN(1.value)
        assertEquals("MIN(?)", field.toSQL(PSQL))
        assertEquals(listOf(1), field.params())
    }
}