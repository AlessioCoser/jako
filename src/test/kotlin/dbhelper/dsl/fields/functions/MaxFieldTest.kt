package dbhelper.dsl.fields.functions

import dbhelper.dsl.fields.As.Companion.AS
import dbhelper.dsl.fields.functions.Avg.Companion.AVG
import dbhelper.dsl.fields.functions.Coalesce.Companion.COALESCE
import dbhelper.dsl.fields.Column.Companion.col
import dbhelper.dsl.fields.Fields
import dbhelper.dsl.fields.functions.Count.Companion.COUNT
import dbhelper.dsl.fields.functions.Every.Companion.EVERY
import dbhelper.dsl.fields.functions.Max.Companion.MAX
import dbhelper.dsl.fields.functions.Min.Companion.MIN
import dbhelper.dsl.fields.Raw.Companion.raw
import dbhelper.dsl.fields.functions.Sum.Companion.SUM
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MaxFieldTest {
    @Test
    fun `MAX field with string`() {
        val field = MAX("table.value")
        assertEquals("MAX(\"table\".\"value\")", field.toString())
    }

    @Test
    fun `MAX field with column field`() {
        val field = MAX("column".col)
        assertEquals("MAX(\"column\")", field.toString())
    }
}