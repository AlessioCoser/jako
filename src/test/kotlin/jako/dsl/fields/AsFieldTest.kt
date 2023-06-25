package jako.dsl.fields

import jako.dsl.Dialect.All.PSQL
import jako.dsl.fields.functions.COUNT
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AsFieldTest {
    @Test
    fun `string AS field`() {
        val field = "column" AS "c"
        assertEquals("\"column\" AS \"c\"", field.toSQL(PSQL))
    }

    @Test
    fun `field AS field`() {
        val field = COUNT("column") AS "c"
        assertEquals("COUNT(\"column\") AS \"c\"", field.toSQL(PSQL))
    }

    @Test
    fun `wrap table-column field with as`() {
        val field = "table.order_index" AS "ord"

        assertEquals("\"table\".\"order_index\" AS \"ord\"", field.toSQL(PSQL))
    }
}