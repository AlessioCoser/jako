package dbhelper.dsl.fields.functions

import dbhelper.dsl.fields.Column.Companion.col
import dbhelper.dsl.fields.functions.Avg.Companion.AVG
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AvgFieldTest {
    @Test
    fun `AVG field with string`() {
        val field = AVG("table.value")
        assertEquals("AVG(\"table\".\"value\")", field.toString())
    }

    @Test
    fun `AVG field with column field`() {
        val field = AVG("column".col)
        assertEquals("AVG(\"column\")", field.toString())
    }
}
