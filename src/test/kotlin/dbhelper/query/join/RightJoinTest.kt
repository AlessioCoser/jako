package dbhelper.query.join

import dbhelper.query.join.Join.Companion.rightJoin
import dbhelper.query.join.Join.Companion.on
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RightJoinTest {
    @Test
    fun `parse right join`() {
        val join = RightJoin("table", "field_original", "field_table")

        assertEquals("RIGHT JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `parse right join USING`() {
        val join = RightJoin("table", "field")

        assertEquals("RIGHT JOIN table USING(field)", join.statement())
    }

    @Test
    fun `right join with rightJoin`() {
        val join = "table" rightJoin "field_original" on "field_table"

        assertEquals("RIGHT JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `right join with USING`() {
        val join = "table" rightJoin "field"

        assertEquals("RIGHT JOIN table USING(field)", join.statement())
    }
}
