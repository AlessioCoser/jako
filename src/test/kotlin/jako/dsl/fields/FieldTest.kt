package jako.dsl.fields

import jako.dsl.fields.functions.Coalesce.Companion.COALESCE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FieldTest {
    @Test
    fun `plus on field`() {
        val field = Column("int_col") + 1

        assertEquals("\"int_col\" + 1", field.toString())
    }

    @Test
    fun `plus field pass params`() {
        val field = COALESCE("column", "value") + 1

        assertEquals("COALESCE(\"column\", ?) + 1", field.toString())
        assertEquals(listOf("value"), field.params())
    }

    @Test
    fun `minus on field`() {
        val field = Raw("int_col") - 1

        assertEquals("int_col - 1", field.toString())
    }

    @Test
    fun `minus field pass params`() {
        val field = COALESCE("column", "value") - 1

        assertEquals("COALESCE(\"column\", ?) - 1", field.toString())
        assertEquals(listOf("value"), field.params())
    }
}