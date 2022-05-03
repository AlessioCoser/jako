package jako.dsl.conditions

import jako.dsl.conditions.And.Companion.AND
import jako.dsl.conditions.Eq.Companion.EQ
import jako.dsl.conditions.Gt.Companion.GT
import jako.dsl.conditions.Or.Companion.OR
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConditionTest {
    @Test
    fun `complex condition logic`() {
        val condition = (("field" EQ "value") OR ("field2" GT 2)) AND ("field3" EQ "value3")

        assertEquals("((\"field\" = ? OR \"field2\" > ?) AND \"field3\" = ?)", condition.toString())
        assertEquals(listOf("value", 2, "value3"), condition.params())
    }

    @Test
    fun `same order different grouping logic`() {
        val condition = ("field" EQ "value") OR (("field2" GT 2) AND ("field3" EQ "value3"))

        assertEquals("(\"field\" = ? OR (\"field2\" > ? AND \"field3\" = ?))", condition.toString())
        assertEquals(listOf("value", 2, "value3"), condition.params())
    }
}