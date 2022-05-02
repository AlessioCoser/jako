package dbhelper.dsl.fields

import dbhelper.dsl.fields.As.Companion.AS
import dbhelper.dsl.fields.functions.Avg.Companion.AVG
import dbhelper.dsl.fields.functions.Coalesce.Companion.COALESCE
import dbhelper.dsl.fields.Column.Companion.col
import dbhelper.dsl.fields.functions.Count.Companion.COUNT
import dbhelper.dsl.fields.functions.Every.Companion.EVERY
import dbhelper.dsl.fields.functions.Max.Companion.MAX
import dbhelper.dsl.fields.functions.Min.Companion.MIN
import dbhelper.dsl.fields.Raw.Companion.raw
import dbhelper.dsl.fields.functions.Sum.Companion.SUM
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
        val fields = Fields(COUNT("column") AS "c")
        assertEquals("COUNT(\"column\") AS \"c\"", fields.toString())
    }
}