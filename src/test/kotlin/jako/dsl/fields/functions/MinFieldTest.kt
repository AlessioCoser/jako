package jako.dsl.fields.functions

import jako.dsl.fields.col
import jako.dsl.fields.value
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MinFieldTest {
    @Test
    fun `MIN field with string`() {
        val field = MIN("table.value")
        assertEquals("MIN(\"table\".\"value\")", field.toSQL())
    }

    @Test
    fun `MIN field with column field`() {
        val field = MIN("column".col)
        assertEquals("MIN(\"column\")", field.toSQL())
        assertEquals(emptyList<Any?>(), field.params())
    }

    @Test
    fun `MIN field with Value field`() {
        val field = MIN(1.value)
        assertEquals("MIN(?)", field.toSQL())
        assertEquals(listOf(1), field.params())
    }
}