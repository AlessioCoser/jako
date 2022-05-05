package jako.dsl.conditions

import jako.dsl.conditions.EQ
import jako.dsl.conditions.GT
import jako.dsl.conditions.OR
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OrConditionTest {
    @Test
    fun `or with different conditions`() {
        val condition = Or(Eq("field", "value"), Gt("field2", 2))

        assertEquals("(\"field\" = ? OR \"field2\" > ?)", condition.toString())
        assertEquals(listOf("value", 2), condition.params())
    }

    @Test
    fun `or using dsl`() {
        val condition = ("field" EQ "value") OR ("field2" GT 2)

        assertEquals("(\"field\" = ? OR \"field2\" > ?)", condition.toString())
        assertEquals(listOf("value", 2), condition.params())
    }
}