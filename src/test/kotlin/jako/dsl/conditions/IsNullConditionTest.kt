package jako.dsl.conditions

import jako.dsl.conditions.IsNull.Companion.IS
import jako.dsl.fields.Column
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class IsNullConditionTest {
    @Test
    fun `string is null`() {
        val condition = IsNull("field")

        assertEquals("\"field\" IS NULL", condition.toString())
        assertEquals(emptyList<Any?>(), condition.params())
    }

    @Test
    fun `string is null using dsl`() {
        val condition = "field" IS null

        assertEquals("\"field\" IS NULL", condition.toString())
    }

    @Test
    fun `field is null using dsl`() {
        val condition = Column("field") IS null

        assertEquals("\"field\" IS NULL", condition.toString())
    }
}