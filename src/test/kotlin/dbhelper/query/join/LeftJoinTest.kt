package dbhelper.query.join

import dbhelper.query.join.On.Companion.eq
import dbhelper.query.join.On.Companion.on
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LeftJoinTest {
    @Test
    fun `parse left join`() {
        val join = LeftJoin(On("table", "field_original", "field_table"))

        assertEquals("LEFT JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `parse left join USING`() {
        val join = LeftJoin(On("table", "field"))

        assertEquals("LEFT JOIN table USING(field)", join.statement())
    }

    @Test
    fun `left join with leftJoin`() {
        val join = LeftJoin("table" on "field_original" eq "field_table")

        assertEquals("LEFT JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `left join with USING`() {
        val join = LeftJoin("table" on "field")

        assertEquals("LEFT JOIN table USING(field)", join.statement())
    }
}
