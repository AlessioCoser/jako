package dbhelper.query

import dbhelper.query.order.Asc
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class QueryBuilderSqlTest {
    @Test
    fun `cannot build query without table name`() {
        val message = assertThrows(RuntimeException::class.java) {
            val query = QueryBuilderSql().build()
        }.message

        assertEquals("Cannot generate query without table name", message)
    }

    @Test
    fun `build simple query`() {
        val query = QueryBuilderSql().from("people").build()

        assertEquals(Query("SELECT * FROM people WHERE true", emptyList()), query)
    }

    @Test
    fun `build query choosing fields to select`() {
        val query = QueryBuilderSql()
            .from("people")
            .fields("first", "second")
            .build()

        assertEquals(Query("SELECT first, second FROM people WHERE true", emptyList()), query)
    }

    @Test
    fun `add order by asc clause`() {
        val query = QueryBuilderSql()
            .from("people")
            .orderBy(Asc("first", "second"))
            .build()

        assertEquals(Query("SELECT * FROM people WHERE true ORDER BY first ASC, second ASC", emptyList()), query)
    }
}