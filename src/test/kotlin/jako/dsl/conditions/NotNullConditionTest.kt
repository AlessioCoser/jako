package jako.dsl.conditions

import jako.dsl.Dialect.All.MYSQL
import jako.dsl.fields.Column
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NotNullConditionTest {
    @Test
    fun `string is not null`() {
        val condition = NotNull("field")

        assertEquals("\"field\" IS NOT NULL", condition.toSQL())
        assertEquals("`field` IS NOT NULL", condition.toSQL(MYSQL))
        assertEquals(emptyList<Any?>(), condition.params())
    }

    @Test
    fun `string is not null using dsl`() {
        val condition = "field" NOT null

        assertEquals("\"field\" IS NOT NULL", condition.toSQL())
    }

    @Test
    fun `field is not null using dsl`() {
        val condition = Column("field") NOT null

        assertEquals("\"field\" IS NOT NULL", condition.toSQL())
    }
}