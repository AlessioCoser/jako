package dbhelper.query

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class QueryBuilderSqlTest {
    @Test
    fun `cannot generate query without table name`() {
        val message = assertThrows(RuntimeException::class.java) {
            val query = QueryBuilderSql().build()
        }.message

        assertEquals("Cannot generate query without table name", message)
    }
}