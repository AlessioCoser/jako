package jako.dsl.query.having

import jako.dsl.conditions.EQ
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GenericHavingTest {
    @Test
    fun `empty having`() {
        val having = NoHaving()

        assertEquals("", having.toString())
        assertEquals(emptyList<Any?>(), having.params())
    }

    @Test
    fun `having statement`() {
        val having = GenericHaving("test" EQ "value")

        assertEquals(" HAVING \"test\" = ?", having.toString())
        assertEquals(listOf("value"), having.params())
    }
}