package dbhelper.query.join

import dbhelper.query.join.InnerJoin.Companion.innerJoin
import dbhelper.query.join.InnerJoin.Companion.join
import dbhelper.query.join.Join.Companion.on
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InnerJoinTest {
    @Test
    fun `parse inner join`() {
        val join = InnerJoin("table", "field_original", "field_table")

        assertEquals("INNER JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `parse inner join USING`() {
        val join = InnerJoin("table", "field")

        assertEquals("INNER JOIN table USING(field)", join.statement())
    }

    @Test
    fun `inner join with innerJoin`() {
        val join = "table" innerJoin "field_original" on "field_table"

        assertEquals("INNER JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `inner join with innerJoin2`() {
        val join = "table" join "field_original" on "field_table"

        assertEquals("INNER JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `inner join with USING`() {
        val join = "table" join "field"

        assertEquals("INNER JOIN table USING(field)", join.statement())
    }
}
