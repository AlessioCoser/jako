package jako.dsl.query.limit

import jako.dsl.Dialect.All.PSQL
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LimitToTest {
    @Test
    fun `no limit`() {
        assertEquals("", NoLimit().toSQL(PSQL))
    }

    @Test
    fun `limit starting from the first element`() {
        val limit = LimitTo(18)

        assertEquals("LIMIT 18", limit.toSQL(PSQL))
    }

    @Test
    fun `limit with offset`() {
        val limit = LimitTo(18, 6)

        assertEquals("LIMIT 18 OFFSET 6", limit.toSQL(PSQL))
    }
}