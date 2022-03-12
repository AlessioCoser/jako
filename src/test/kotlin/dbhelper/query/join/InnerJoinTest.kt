package dbhelper.query.join

import dbhelper.query.conditions.Eq
import dbhelper.query.conditions.Eq.Companion.eq
import dbhelper.query.join.InnerJoin.Companion.innerJoin
import dbhelper.query.join.InnerJoin.Companion.join
import dbhelper.query.join.InnerJoin.Companion.on
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InnerJoinTest {
    @Test
    fun `parse inner join`() {
        val join = InnerJoin("table", Eq("field_original", "field_table"))

        assertEquals("INNER JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `inner join with on`() {
        val join = "table" on ("field_original" eq "field_table")

        assertEquals("INNER JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `inner join with join`() {
        val join = "table" join ("field_original" eq "field_table")

        assertEquals("INNER JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `inner join with innerJoin`() {
        val join = "table" innerJoin ("field_original" eq "field_table")

        assertEquals("INNER JOIN table ON field_original = field_table", join.statement())
    }
}