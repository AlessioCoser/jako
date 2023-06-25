package jako.dsl.conditions

import jako.dsl.Dialect.All.MYSQL
import jako.dsl.Dialect.All.PSQL
import jako.dsl.fields.Column
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class IsNullConditionTest {
    @Test
    fun `string is null`() {
        val condition = IsNull("field")

        assertEquals("\"field\" IS NULL", condition.toSQL(PSQL))
        assertEquals("`field` IS NULL", condition.toSQL(MYSQL))
        assertEquals(emptyList<Any?>(), condition.params())
    }

    @Test
    fun `string is null using dsl`() {
        val condition = "field" IS null

        assertEquals("\"field\" IS NULL", condition.toSQL(PSQL))
    }

    @Test
    fun `field is null using dsl`() {
        val condition = Column("field") IS null

        assertEquals("\"field\" IS NULL", condition.toSQL(PSQL))
    }
}