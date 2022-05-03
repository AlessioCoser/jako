package jako.dsl.fields

import jako.dsl.fields.As.Companion.AS
import jako.dsl.fields.functions.Count.Companion.COUNT
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AsFieldTest {
    @Test
    fun `string AS field`() {
        val field = "column" AS "c"
        assertEquals("\"column\" AS \"c\"", field.toString())
    }

    @Test
    fun `field AS field`() {
        val field = COUNT("column") AS "c"
        assertEquals("COUNT(\"column\") AS \"c\"", field.toString())
    }

    @Test
    fun `wrap table-column field with as`() {
        val field = "table.order_index" AS "ord"

        assertEquals("\"table\".\"order_index\" AS \"ord\"", field.toString())
    }
}