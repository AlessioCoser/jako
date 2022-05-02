package dbhelper.dsl.fields.functions

import dbhelper.dsl.fields.Column.Companion.col
import dbhelper.dsl.fields.functions.Count.Companion.COUNT
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CountFieldTest {
    @Test
    fun `COUNT field with table-value`() {
        val field = COUNT("table.value")
        assertEquals("COUNT(\"table\".\"value\")", field.toString())
    }

    @Test
    fun `COUNT field with column as field`() {
        val field = COUNT("column".col)
        assertEquals("COUNT(\"column\")", field.toString())
    }
}
