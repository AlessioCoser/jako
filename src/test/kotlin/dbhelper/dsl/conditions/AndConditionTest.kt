package dbhelper.dsl.conditions

import dbhelper.dsl.conditions.And.Companion.AND
import dbhelper.dsl.conditions.Eq.Companion.EQ
import dbhelper.dsl.conditions.Gt.Companion.GT
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AndConditionTest {
    @Test
    fun `and`() {
        val condition = And(Eq("field", "value"), Eq("field2", "value2"))

        assertEquals("(\"field\" = ? AND \"field2\" = ?)", condition.toString())
        assertEquals(listOf("value", "value2"), condition.params())
    }

    @Test
    fun `and with different conditions`() {
        val condition = And(Eq("field", "value"), Gt("field2", 2))

        assertEquals("(\"field\" = ? AND \"field2\" > ?)", condition.toString())
        assertEquals(listOf("value", 2), condition.params())
    }

    @Test
    fun `and using dsl`() {
        val condition = ("field" EQ "value") AND ("field2" GT 2)

        assertEquals("(\"field\" = ? AND \"field2\" > ?)", condition.toString())
        assertEquals(listOf("value", 2), condition.params())
    }
}