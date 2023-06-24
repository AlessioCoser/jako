package jako.dsl.fields.functions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoalesceFieldTest {
    @Test
    fun `coalesce with string and int default value`() {
        val field = COALESCE("table.order_index", 1)

        assertEquals("COALESCE(\"table\".\"order_index\", ?)", field.toSQL())
        assertEquals(listOf(1), field.params())
    }

    @Test
    fun `coalesce with field`() {
        val field = COALESCE(MAX("order_index"), 1)

        assertEquals("COALESCE(MAX(\"order_index\"), ?)", field.toSQL())
        assertEquals(listOf(1), field.params())
    }

    @Test
    fun `coalesce with string default value`() {
        val field = COALESCE("order_index", "string")

        assertEquals("COALESCE(\"order_index\", ?)", field.toSQL())
        assertEquals(listOf("string"), field.params())
    }
}