package jako.dsl.fields

import jako.dsl.Dialect.All.PSQL
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ColumnFieldTest {
    @Test
    fun `create columns from string`() {
        val fields = "column".col
        assertEquals("\"column\"", fields.toSQL(PSQL))
    }

    @Test
    fun `field with asterisk`() {
        val fields = Column("*")
        assertEquals("*", fields.toSQL(PSQL))
    }

    @Test
    fun `field with simple text`() {
        val fields = Column("primo")
        assertEquals("\"primo\"", fields.toSQL(PSQL))
    }

    @Test
    fun `do quote table-column`() {
        val field = Column("primo.secondo")
        assertEquals("\"primo\".\"secondo\"", field.toSQL(PSQL))
    }

    @Test
    fun `do not quote when already present an aggregate function`() {
        val field = Column("count(\"primo\")")
        assertEquals("count(\"primo\")", field.toSQL(PSQL))
    }

    @Test
    fun `do quote only inside when already present an aggregate function`() {
        val field = Column("count(primo.secondo)")
        assertEquals("count(\"primo\".\"secondo\")", field.toSQL(PSQL))
    }
}
