package dbhelper.dsl.conditions

import dbhelper.dsl.conditions.NotNull.Companion.NOT
import dbhelper.dsl.fields.Column
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NotNullConditionTest {
    @Test
    fun `string is not null`() {
        val condition = NotNull("field")

        assertEquals("\"field\" IS NOT NULL", condition.toString())
        assertEquals(emptyList<Any?>(), condition.params())
    }

    @Test
    fun `string is not null using dsl`() {
        val condition = "field" NOT null

        assertEquals("\"field\" IS NOT NULL", condition.toString())
    }

    @Test
    fun `field is not null using dsl`() {
        val condition = Column("field") NOT null

        assertEquals("\"field\" IS NOT NULL", condition.toString())
    }
}