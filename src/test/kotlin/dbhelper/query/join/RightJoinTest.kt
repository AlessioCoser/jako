package dbhelper.query.join

import dbhelper.query.join.On.Companion.eq
import dbhelper.query.join.On.Companion.on
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RightJoinTest {
    @Test
    fun `parse right join`() {
        val join = RightJoin(On("table", "field_original", "field_table"))

        assertEquals("RIGHT JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `parse right join USING`() {
        val join = RightJoin(On("table", "field"))

        assertEquals("RIGHT JOIN table USING(field)", join.statement())
    }

    @Test
    fun `right join with rightJoin`() {
        val join = RightJoin("table" on "field_original" eq "field_table")

        assertEquals("RIGHT JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `right join with USING`() {
        val join = RightJoin("table" on "field")

        assertEquals("RIGHT JOIN table USING(field)", join.statement())
    }
}
