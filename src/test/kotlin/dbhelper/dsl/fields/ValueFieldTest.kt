package dbhelper.dsl.fields

import dbhelper.dsl.fields.Value.Companion.value
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ValueFieldTest {
    @Test
    fun `value using dsl with int param`() {
        val field: Value = 12.value
        assertEquals("?", field.toString())
        assertEquals(listOf<Any?>(12), field.params())
    }

    @Test
    fun `field with asterisk`() {
        val field = Value("total")
        assertEquals("?", field.toString())
        assertEquals(listOf<Any?>("total"), field.params())
    }
}
