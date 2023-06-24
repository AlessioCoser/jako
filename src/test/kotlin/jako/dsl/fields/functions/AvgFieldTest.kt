package jako.dsl.fields.functions

import jako.dsl.fields.col
import jako.dsl.fields.value
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AvgFieldTest {
    @Test
    fun `AVG field with string`() {
        val field = AVG("table.value")
        assertEquals("AVG(\"table\".\"value\")", field.toSQL())
        assertEquals(emptyList<Any?>(), field.params())
    }

    @Test
    fun `AVG field with column field`() {
        val field = AVG("column".col)
        assertEquals("AVG(\"column\")", field.toSQL())
        assertEquals(emptyList<Any?>(), field.params())
    }

    @Test
    fun `AVG field with Value field`() {
        val field = AVG(1.value)
        assertEquals("AVG(?)", field.toSQL())
        assertEquals(listOf(1), field.params())
    }
}
