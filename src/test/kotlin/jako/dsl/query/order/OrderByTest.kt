package jako.dsl.query.order

import jako.dsl.Dialect.All.PSQL
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OrderByTest {
    @Test
    fun `no order`() {
        assertEquals("", NoOrder().toSQL(PSQL))
    }

    @Test
    fun `order by one field`() {
        val orderBy = OrderBy(listOf(Asc("field1")))

        assertEquals("ORDER BY \"field1\" ASC", orderBy.toSQL(PSQL))
    }

    @Test
    fun `order by two fields`() {
        val orderBy = OrderBy(listOf(Asc("field1"), Desc("field2")))

        assertEquals("ORDER BY \"field1\" ASC, \"field2\" DESC", orderBy.toSQL(PSQL))
    }
}