package dbhelper.dsl.query.join

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JoinsTest {
    @Test
    fun `empty joins`() {
        val joins = Joins()

        assertEquals("", joins.toString())
    }

    @Test
    fun `joins with one join`() {
        val joins = Joins()
        joins.join(On("table", "field1"))

        assertEquals(" INNER JOIN \"table\" USING(\"field1\")", joins.toString())
    }

    @Test
    fun `joins with two joins`() {
        val joins = Joins()
        joins.join(On("table", "field1"))
        joins.join(On("another", "field2"))

        assertEquals(
            " INNER JOIN \"table\" USING(\"field1\")" +
            " INNER JOIN \"another\" USING(\"field2\")",
            joins.toString()
        )
    }

    @Test
    fun `joins with one left join`() {
        val joins = Joins()
        joins.leftJoin(On("table", "field1"))

        assertEquals(" LEFT JOIN \"table\" USING(\"field1\")", joins.toString())
    }

    @Test
    fun `joins with one right join`() {
        val joins = Joins()
        joins.rightJoin(On("table", "field1"))

        assertEquals(" RIGHT JOIN \"table\" USING(\"field1\")", joins.toString())
    }
}