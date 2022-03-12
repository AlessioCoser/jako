package dbhelper.query.join

import dbhelper.query.join.Join.Companion.leftJoin
import dbhelper.query.join.Join.Companion.on
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LeftJoinTest {
    @Test
    fun `parse left join`() {
        val join = LeftJoin("table", "field_original", "field_table")

        assertEquals("LEFT JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `parse left join USING`() {
        val join = LeftJoin("table", "field")

        assertEquals("LEFT JOIN table USING(field)", join.statement())
    }

    @Test
    fun `left join with leftJoin`() {
        val join = "table" leftJoin "field_original" on "field_table"

        assertEquals("LEFT JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `left join with USING`() {
        val join = "table" leftJoin "field"

        assertEquals("LEFT JOIN table USING(field)", join.statement())
    }
}
