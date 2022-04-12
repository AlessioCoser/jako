package dbhelper.dsl.fields

import dbhelper.dsl.fields.Fields.Companion.AS
import dbhelper.dsl.fields.Fields.Companion.AVG
import dbhelper.dsl.fields.Fields.Companion.COUNT
import dbhelper.dsl.fields.Fields.Companion.EVERY
import dbhelper.dsl.fields.Fields.Companion.MAX
import dbhelper.dsl.fields.Fields.Companion.MIN
import dbhelper.dsl.fields.Fields.Companion.SUM
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FieldsTest {
    @Test
    fun `fields with simple texts`() {
        val fields = Fields(listOf("primo", "secondo"))
        assertEquals("\"primo\", \"secondo\"", fields.toString())
    }

    @Test
    fun `fields with AS and aggregate function`() {
        val fields = Fields(listOf("primo" AS "p", "primo.secondo" AS "s", COUNT("secondo") AS "c"))
        assertEquals("\"primo\" AS \"p\", \"primo\".\"secondo\" AS \"s\", count(\"secondo\") AS \"c\"", fields.toString())
    }

    @Test
    fun `fields with COUNT and asterisk`() {
        val fields = Fields(listOf(COUNT("*") AS "c"))
        assertEquals("count(*) AS \"c\"", fields.toString())
    }

    @Test
    fun `fields with AVG`() {
        val fields = Fields(listOf(AVG("field") AS "name"))
        assertEquals("avg(\"field\") AS \"name\"", fields.toString())
    }

    @Test
    fun `fields with EVERY`() {
        val fields = Fields(listOf(EVERY("field") AS "name"))
        assertEquals("every(\"field\") AS \"name\"", fields.toString())
    }

    @Test
    fun `fields with MAX`() {
        val fields = Fields(listOf(MAX("field") AS "name"))
        assertEquals("max(\"field\") AS \"name\"", fields.toString())
    }

    @Test
    fun `fields with MIN`() {
        val fields = Fields(listOf(MIN("field") AS "name"))
        assertEquals("min(\"field\") AS \"name\"", fields.toString())
    }

    @Test
    fun `fields with SUM`() {
        val fields = Fields(listOf(SUM("field") AS "name"))
        assertEquals("sum(\"field\") AS \"name\"", fields.toString())
    }

    @Test
    fun `do not quote when already present an aggregate function`() {
        val fields = Fields(listOf("count(\"primo\")"))
        assertEquals("count(\"primo\")", fields.toString())
    }

    @Test
    fun `do quote only inside when already present an aggregate function`() {
        val fields = Fields(listOf("count(primo.secondo)"))
        assertEquals("count(\"primo\".\"secondo\")", fields.toString())
    }
}