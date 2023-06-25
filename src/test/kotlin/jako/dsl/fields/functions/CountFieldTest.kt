package jako.dsl.fields.functions

import jako.dsl.Dialect.All.PSQL
import jako.dsl.fields.col
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CountFieldTest {
    @Test
    fun `COUNT field with table-value`() {
        val field = COUNT("table.value")
        assertEquals("COUNT(\"table\".\"value\")", field.toSQL(PSQL))
    }

    @Test
    fun `COUNT field with column as field`() {
        val field = COUNT("column".col)
        assertEquals("COUNT(\"column\")", field.toSQL(PSQL))
    }
}
