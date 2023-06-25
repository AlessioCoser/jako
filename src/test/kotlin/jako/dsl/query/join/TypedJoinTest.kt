package jako.dsl.query.join

import jako.dsl.Dialect.All.PSQL
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TypedJoinTest {
    @Test
    fun `parse inner join`() {
        val join = InnerJoin(On("table", "field_original", "field_table"))

        assertEquals("""INNER JOIN "table" ON "field_original" = "field_table"""", join.toSQL(PSQL))
    }

    @Test
    fun `parse inner join USING`() {
        val join = InnerJoin(On("table", "field"))

        assertEquals("""INNER JOIN "table" USING("field")""", join.toSQL(PSQL))
    }

    @Test
    fun `inner join with innerJoin`() {
        val join = InnerJoin("table" ON "field_original" EQ "field_table")

        assertEquals("""INNER JOIN "table" ON "field_original" = "field_table"""", join.toSQL(PSQL))
    }

    @Test
    fun `inner join with USING`() {
        val join = InnerJoin("table" ON "field")

        assertEquals("""INNER JOIN "table" USING("field")""", join.toSQL(PSQL))
    }

    @Test
    fun `left join`() {
        val join = LeftJoin("table" ON "field")

        assertEquals("""LEFT JOIN "table" USING("field")""", join.toSQL(PSQL))
    }

    @Test
    fun `right join`() {
        val join = RightJoin("table" ON "field")

        assertEquals("""RIGHT JOIN "table" USING("field")""", join.toSQL(PSQL))
    }
}
