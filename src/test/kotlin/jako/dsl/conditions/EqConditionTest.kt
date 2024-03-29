package jako.dsl.conditions

import jako.dsl.Dialect.All.MYSQL
import jako.dsl.Dialect.All.PSQL
import jako.dsl.fields.col
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EqConditionTest {
    @Test
    fun `eq as int`() {
        val condition = Eq("field", 2)

        assertEquals("\"field\" = ?", condition.toSQL(PSQL))
        assertEquals("`field` = ?", condition.toSQL(MYSQL))
        assertEquals(listOf(2), condition.params())
    }

    @Test
    fun `eq as string`() {
        val condition = Eq("field", "value")

        assertEquals("\"field\" = ?", condition.toSQL(PSQL))
        assertEquals(listOf("value"), condition.params())
    }

    @Test
    fun `eq str using dsl`() {
        val condition = "field" EQ "value"

        assertEquals("\"field\" = ?", condition.toSQL(PSQL))
        assertEquals("`field` = ?", condition.toSQL(MYSQL))
        assertEquals(listOf("value"), condition.params())
    }

    @Test
    fun `eq str using field dsl`() {
        val condition = "field".col EQ "value"

        assertEquals("\"field\" = ?", condition.toSQL(PSQL))
        assertEquals("`field` = ?", condition.toSQL(MYSQL))
        assertEquals(listOf("value"), condition.params())
    }

    @Test
    fun `eq int using dsl`() {
        val condition = "field" EQ 1

        assertEquals("\"field\" = ?", condition.toSQL(PSQL))
        assertEquals(listOf(1), condition.params())
    }

    @Test
    fun `eq int using field dsl`() {
        val condition = "field".col EQ 1

        assertEquals("\"field\" = ?", condition.toSQL(PSQL))
        assertEquals(listOf(1), condition.params())
    }
}