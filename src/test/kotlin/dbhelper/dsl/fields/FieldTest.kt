package dbhelper.dsl.fields

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FieldTest {
    @Test
    fun `plus on field`() {
        val field = Column("int_col") + 1

        assertEquals("\"int_col\" + 1", field.toString())
    }

    @Test
    fun `minus on field`() {
        val field = Raw("int_col") - 1

        assertEquals("int_col - 1", field.toString())
    }
}