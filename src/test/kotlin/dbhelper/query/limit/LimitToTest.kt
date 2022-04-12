package dbhelper.query.limit

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LimitToTest {
    @Test
    fun `no limit`() {
        assertEquals("", NoLimit().toString())
    }

    @Test
    fun `limit starting from the first element`() {
        val limit = LimitTo(18)

        assertEquals(" LIMIT 18", limit.toString())
    }

    @Test
    fun `limit with offset`() {
        val limit = LimitTo(18, 6)

        assertEquals(" LIMIT 18 OFFSET 6", limit.toString())
    }
}