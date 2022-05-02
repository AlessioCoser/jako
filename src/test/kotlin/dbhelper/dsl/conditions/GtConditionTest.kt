package dbhelper.dsl.conditions

import dbhelper.dsl.conditions.Gt.Companion.GT
import dbhelper.dsl.fields.Column.Companion.col
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GtConditionTest {
    @Test
    fun gt() {
        val condition = Gt("field", 2)

        assertEquals("\"field\" > ?", condition.toString())
        assertEquals(listOf(2), condition.params())
    }

    @Test
    fun `gt using dsl`() {
        val condition = "field" GT 2

        assertEquals("\"field\" > ?", condition.toString())
        assertEquals(listOf(2), condition.params())
    }

    @Test
    fun `gt using field dsl`() {
        val condition = "field".col GT 2

        assertEquals("\"field\" > ?", condition.toString())
        assertEquals(listOf(2), condition.params())
    }
}