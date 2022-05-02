package dbhelper.dsl.fields.functions

import dbhelper.dsl.fields.functions.Coalesce.Companion.COALESCE
import dbhelper.dsl.fields.functions.Max.Companion.MAX
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoalesceFieldTest {
    @Test
    fun `coalesce with string and int default value`() {
        val field = COALESCE("table.order_index", 1)

        assertEquals("COALESCE(\"table\".\"order_index\", ?)", field.toString())
        assertEquals(listOf(1), field.params())
    }

    @Test
    fun `coalesce with field`() {
        val field = COALESCE(MAX("order_index"), 1)

        assertEquals("COALESCE(MAX(\"order_index\"), ?)", field.toString())
        assertEquals(listOf(1), field.params())
    }

    @Test
    fun `coalesce with string default value`() {
        val field = COALESCE("order_index", "string")

        assertEquals("COALESCE(\"order_index\", ?)", field.toString())
        assertEquals(listOf("string"), field.params())
    }
}