package dbhelper.query.conditions

import dbhelper.query.conditions.And.Companion.AND
import dbhelper.query.conditions.Eq.Companion.EQ
import dbhelper.query.conditions.Gt.Companion.GT
import dbhelper.query.conditions.Or.Companion.OR
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConditionTest {
    @Test
    fun `eq as int`() {
        val condition = Eq("field", 2)

        assertEquals("\"field\" = ?", condition.toString())
        assertEquals(listOf(2), condition.params())
    }

    @Test
    fun `eq as string`() {
        val condition = Eq("field", "value")

        assertEquals("\"field\" = ?", condition.toString())
        assertEquals(listOf("value"), condition.params())
    }

    @Test
    fun `eq str using dsl`() {
        val condition = "field" EQ "value"

        assertEquals("\"field\" = ?", condition.toString())
        assertEquals(listOf("value"), condition.params())
    }

    @Test
    fun `eq int using dsl`() {
        val condition = "field" EQ 1

        assertEquals("\"field\" = ?", condition.toString())
        assertEquals(listOf(1), condition.params())
    }

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

    @Test
    fun `or and all together using dsl`() {
        val condition = (("field" EQ "value") OR ("field2" GT 2)) AND ("field3" EQ "value3")

        assertEquals("((\"field\" = ? OR \"field2\" > ?) AND \"field3\" = ?)", condition.toString())
        assertEquals(listOf("value", 2, "value3"), condition.params())
    }
}