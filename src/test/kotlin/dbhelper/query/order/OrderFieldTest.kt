package dbhelper.query.order

import dbhelper.query.order.Asc.Companion.ASC
import dbhelper.query.order.Desc.Companion.DESC
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OrderFieldTest {
    @Test
    fun `asc one field`() {
        val asc = Asc("field1")

        assertEquals("\"field1\" ASC", asc.toString())
    }

    @Test
    fun `asc dsl`() {
        val asc = ASC("field1")

        assertEquals("\"field1\" ASC", asc.toString())
    }

    @Test
    fun `asc multiple fields`() {
        val asc = Asc("field1", "field2", "field3")

        assertEquals("\"field1\" ASC, \"field2\" ASC, \"field3\" ASC", asc.toString())
    }

    @Test
    fun `desc one field`() {
        val desc = Desc("field1")

        assertEquals("\"field1\" DESC", desc.toString())
    }

    @Test
    fun `desc dsl`() {
        val asc = DESC("field1")

        assertEquals("\"field1\" DESC", asc.toString())
    }

    @Test
    fun `desc multiple fields`() {
        val desc = Desc("field1", "field2", "field3")

        assertEquals("\"field1\" DESC, \"field2\" DESC, \"field3\" DESC", desc.toString())
    }
}