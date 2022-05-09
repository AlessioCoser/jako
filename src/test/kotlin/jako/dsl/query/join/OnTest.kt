package jako.dsl.query.join

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OnTest {
    @Test
    fun `join on table and one field`() {
        val on = On("table", "field1")

        assertEquals("\"table\" USING(\"field1\")", on.toString())
    }

    @Test
    fun `join on table and two fields`() {
        val on = On("table", "field1", "field2")

        assertEquals("\"table\" ON \"field1\" = \"field2\"", on.toString())
    }

    @Test
    fun `joining on statements`() {
        val on = "table" ON "field1" EQ "field2"

        assertEquals("\"table\" ON \"field1\" = \"field2\"", on.toString())
    }
}
