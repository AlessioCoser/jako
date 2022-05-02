package dbhelper.dsl.fields.functions

import dbhelper.dsl.fields.Column.Companion.col
import dbhelper.dsl.fields.functions.Sum.Companion.SUM
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SumFieldTest {
    @Test
    fun `SUM field with string`() {
        val field = SUM("table.value")
        assertEquals("SUM(\"table\".\"value\")", field.toString())
    }

    @Test
    fun `SUM field with column field`() {
        val field = SUM("column".col)
        assertEquals("SUM(\"column\")", field.toString())
    }
}