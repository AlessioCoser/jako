package dbhelper.dsl.fields.functions

import dbhelper.dsl.fields.Column.Companion.col
import dbhelper.dsl.fields.Value.Companion.value
import dbhelper.dsl.fields.functions.Every.Companion.EVERY
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EveryFieldTest {
    @Test
    fun `EVERY field with string`() {
        val field = EVERY("table.value")
        assertEquals("EVERY(\"table\".\"value\")", field.toString())
        assertEquals(emptyList<Any?>(), field.params())
    }

    @Test
    fun `EVERY field with column field`() {
        val field = EVERY("column".col)
        assertEquals("EVERY(\"column\")", field.toString())
        assertEquals(emptyList<Any?>(), field.params())
    }

    @Test
    fun `EVERY field with Value field`() {
        val field = EVERY(1.value)
        assertEquals("EVERY(?)", field.toString())
        assertEquals(listOf(1), field.params())
    }
}