package dbhelper.dsl.fields.functions

import dbhelper.dsl.fields.Column.Companion.col
import dbhelper.dsl.fields.Value.Companion.value
import dbhelper.dsl.fields.functions.Max.Companion.MAX
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MaxFieldTest {
    @Test
    fun `MAX field with string`() {
        val field = MAX("table.value")
        assertEquals("MAX(\"table\".\"value\")", field.toString())
    }

    @Test
    fun `MAX field with column field`() {
        val field = MAX("column".col)
        assertEquals("MAX(\"column\")", field.toString())
        assertEquals(emptyList<Any?>(), field.params())
    }

    @Test
    fun `MAX field with Value field`() {
        val field = MAX(1.value)
        assertEquals("MAX(?)", field.toString())
        assertEquals(listOf(1), field.params())
    }
}