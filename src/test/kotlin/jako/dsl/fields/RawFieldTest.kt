package jako.dsl.fields

import jako.dsl.fields.functions.COALESCE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RawFieldTest {

    @Test
    fun `raw field does nothing`() {
        val field = Raw("UNEXISTING_FN(order_index)")

        assertEquals("UNEXISTING_FN(order_index)", field.toSQL())
    }

    @Test
    fun `create raw field from string`() {
        val field = "UNEXISTING_FN(order_index)".raw

        assertEquals("UNEXISTING_FN(order_index)", field.toSQL())
    }

    @Test
    fun `raw field pass params`() {
        val field = Raw(COALESCE("column", "value"))

        assertEquals("COALESCE(\"column\", ?)", field.toSQL())
        assertEquals(listOf("value"), field.params())
    }
}
