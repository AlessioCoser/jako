package dbhelper.dsl.fields

import dbhelper.dsl.fields.As.Companion.AS
import dbhelper.dsl.fields.CoalesceField.Companion.COALESCE
import dbhelper.dsl.fields.Column.Companion.C
import dbhelper.dsl.fields.MaxField.Companion.MAX
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FieldsTest {
    @Test
    fun `fields with simple texts`() {
        val fields = Fields("primo", "secondo")
        assertEquals("\"primo\", \"secondo\"", fields.toString())
    }
//
//    @Test
//    fun `fields with AS and aggregate function`() {
//        val fields = Fields(listOf("primo" AS "p", "primo.secondo" AS "s", COUNT("secondo") AS "c"))
//        assertEquals("\"primo\" AS \"p\", \"primo\".\"secondo\" AS \"s\", count(\"secondo\") AS \"c\"", fields.toString())
//    }
//
//    @Test
//    fun `fields with COUNT and asterisk`() {
//        val fields = Fields(listOf(COUNT("*") AS "c"))
//        assertEquals("count(*) AS \"c\"", fields.toString())
//    }
//
//    @Test
//    fun `fields with AVG`() {
//        val fields = Fields(listOf(AVG("field") AS "name"))
//        assertEquals("avg(\"field\") AS \"name\"", fields.toString())
//    }
//
//    @Test
//    fun `fields with EVERY`() {
//        val fields = Fields(listOf(EVERY("field") AS "name"))
//        assertEquals("every(\"field\") AS \"name\"", fields.toString())
//    }
//
//    @Test
//    fun `fields with MAX`() {
//        val fields = Fields(listOf(MAX("field") AS "name"))
//        assertEquals("max(\"field\") AS \"name\"", fields.toString())
//    }
//
//    @Test
//    fun `fields with MIN`() {
//        val fields = Fields(listOf(MIN("field") AS "name"))
//        assertEquals("min(\"field\") AS \"name\"", fields.toString())
//    }
//
//    @Test
//    fun `fields with SUM`() {
//        val fields = Fields(listOf(SUM("field") AS "name"))
//        assertEquals("sum(\"field\") AS \"name\"", fields.toString())
//    }

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
    fun yyy() {
        val field = COALESCE(MAX("order_index"), -1) + 1 AS "PIPPO"

        assertEquals("COALESCE(MAX(\"order_index\"), -1) + 1 AS \"PIPPO\"", field.toString())
    }

    @Test
    fun zzz() {
        val field = "table.order_index" AS "ord"

        assertEquals("\"table\".\"order_index\" AS \"ord\"", field.toString())
    }
}