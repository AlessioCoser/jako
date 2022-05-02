package dbhelper.dsl.conditions

import dbhelper.dsl.fields.functions.Coalesce.Companion.COALESCE
import dbhelper.dsl.fields.functions.Max.Companion.MAX
import dbhelper.dsl.fields.functions.Min.Companion.MIN
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoalesceFieldTest {
    @Test
    fun `coalesce with string and int default value`() {
        val field = COALESCE("table.order_index", 1)

        assertEquals("COALESCE(\"table\".\"order_index\", 1)", field.toString())
    }

    @Test
    fun `coalesce with field`() {
        val field = COALESCE(MAX("order_index"), 1)

        assertEquals("COALESCE(MAX(\"order_index\"), 1)", field.toString())
    }

//    @Test
//    fun `coalesce with string default value`() {
//        val field = COALESCE("order_index", "string")
//
//        assertEquals("COALESCE(\"order_index\", 'string')", field.toString())
//    }
}