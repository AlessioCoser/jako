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

class FieldsTest {
    @Test
    fun `fields with simple texts`() {
        val fields = Fields("primo", "secondo")
        assertEquals("\"primo\", \"secondo\"", fields.toString())
    }

    @Test
    fun `fields with COUNT and asterisk`() {
        val fields = Fields(COUNT("*") AS "c")
        assertEquals("COUNT(*) AS \"c\"", fields.toString())
    }

    @Test
    fun `fields with COUNT 2 and asterisk`() {
        val fields = Fields(COUNT("*".raw) AS "c")
        assertEquals("COUNT(*) AS \"c\"", fields.toString())
    }

    @Test
    fun `fields with AVG`() {
        val fields = Fields(AVG("field") AS "name")
        assertEquals("AVG(\"field\") AS \"name\"", fields.toString())
    }

    @Test
    fun `fields with AVG 2`() {
        val fields = Fields(AVG("field".col) AS "name")
        assertEquals("AVG(\"field\") AS \"name\"", fields.toString())
    }

    @Test
    fun `fields with EVERY`() {
        val fields = Fields(EVERY("field") AS "name")
        assertEquals("EVERY(\"field\") AS \"name\"", fields.toString())
    }

    @Test
    fun `fields with EVERY 2`() {
        val fields = Fields(EVERY("field".col) AS "name")
        assertEquals("EVERY(\"field\") AS \"name\"", fields.toString())
    }

    @Test
    fun `fields with MAX`() {
        val fields = Fields(MAX("field") AS "name")
        assertEquals("MAX(\"field\") AS \"name\"", fields.toString())
    }

    @Test
    fun `fields with MAX 2`() {
        val fields = Fields(MAX("field".col) AS "name")
        assertEquals("MAX(\"field\") AS \"name\"", fields.toString())
    }

    @Test
    fun `fields with MIN`() {
        val fields = Fields(MIN("field") AS "name")
        assertEquals("MIN(\"field\") AS \"name\"", fields.toString())
    }

    @Test
    fun `fields with MIN 2`() {
        val fields = Fields(MIN("field".col) AS "name")
        assertEquals("MIN(\"field\") AS \"name\"", fields.toString())
    }

    @Test
    fun `fields with SUM`() {
        val fields = Fields(SUM("field") AS "name")
        assertEquals("SUM(\"field\") AS \"name\"", fields.toString())
    }

    @Test
    fun `fields with SUM 2`() {
        val fields = Fields(SUM("field".col) AS "name")
        assertEquals("SUM(\"field\") AS \"name\"", fields.toString())
    }

    @Test
    fun `do not quote when already present an aggregate function`() {
        val fields = Fields("count(\"primo\")")
        assertEquals("count(\"primo\")", fields.toString())
    }

    @Test
    fun `do quote only inside when already present an aggregate function`() {
        val fields = Fields("count(primo.secondo)")
        assertEquals("count(\"primo\".\"secondo\")", fields.toString())
    }

    @Test
    fun xxx() {
        val field = COALESCE(MAX("order_index"), 1) - 1

        assertEquals("COALESCE(MAX(\"order_index\"), 1) - 1", field.toString())
    }

    @Test
    fun xxxx() {
        val field = COALESCE("order_index", 1)

        assertEquals("COALESCE(\"order_index\", 1)", field.toString())
    }

    @Test
    fun yyy() {
        val field = COALESCE(MAX("order_index"), -1) + 1 AS "PIPPO"

        assertEquals("COALESCE(MAX(\"order_index\"), -1) + 1 AS \"PIPPO\"", field.toString())
    }

    @Test
    fun zzz() {
        val field = "table.order_index" AS "ord"

        assertEquals("\"table\".\"order_index\" AS \"ord\"", field.toString())
    }

    @Test
    fun ttt() {
        val field = "UNEXISTING_FN(order_index)".raw

        assertEquals("UNEXISTING_FN(order_index)", field.toString())
    }
}