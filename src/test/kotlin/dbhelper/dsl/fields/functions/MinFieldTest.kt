package dbhelper.dsl.fields.functions

import dbhelper.dsl.fields.Column.Companion.col
import dbhelper.dsl.fields.functions.Min.Companion.MIN
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MinFieldTest {
    @Test
    fun `MIN field with string`() {
        val field = MIN("table.value")
        assertEquals("MIN(\"table\".\"value\")", field.toString())
    }

    @Test
    fun `MIN field with column field`() {
        val field = MIN("column".col)
        assertEquals("MIN(\"column\")", field.toString())
    }
}