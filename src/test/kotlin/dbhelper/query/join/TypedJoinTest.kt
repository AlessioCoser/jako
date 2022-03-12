package dbhelper.query.join

import dbhelper.query.join.On.Companion.eq
import dbhelper.query.join.On.Companion.on
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TypedJoinTest {
    @Test
    fun `parse inner join`() {
        val join = InnerJoin(On("table", "field_original", "field_table"))

        assertEquals("INNER JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `parse inner join USING`() {
        val join = InnerJoin(On("table", "field"))

        assertEquals("INNER JOIN table USING(field)", join.statement())
    }

    @Test
    fun `inner join with innerJoin`() {
        val join = InnerJoin("table" on "field_original" eq "field_table")

        assertEquals("INNER JOIN table ON field_original = field_table", join.statement())
    }

    @Test
    fun `inner join with USING`() {
        val join = InnerJoin("table" on "field")

        assertEquals("INNER JOIN table USING(field)", join.statement())
    }

    @Test
    fun `left join`() {
        val join = LeftJoin("table" on "field")

        assertEquals("LEFT JOIN table USING(field)", join.statement())
    }

    @Test
    fun `right join`() {
        val join = RightJoin("table" on "field")

        assertEquals("RIGHT JOIN table USING(field)", join.statement())
    }
}
