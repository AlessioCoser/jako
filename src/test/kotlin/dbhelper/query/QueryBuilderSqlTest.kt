package dbhelper.query

import dbhelper.query.order.Asc
import dbhelper.query.order.Desc
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

    @Test
    fun `add order by with one asc and one desc clause`() {
        val query = QueryBuilderSql()
            .from("people")
            .orderBy(Asc("first"), Desc("second"))
            .build()

        assertEquals(Query("SELECT * FROM people WHERE true ORDER BY first ASC, second DESC", emptyList()), query)
    }

    @Test
    fun `single limits to one row`() {
        val query = QueryBuilderSql()
            .from("people")
            .single()
            .build()

        assertEquals(Query("SELECT * FROM people WHERE true LIMIT 1", emptyList()), query)
    }

    @Test
    fun `limit clause`() {
        val query = QueryBuilderSql()
            .from("people")
            .limit(34)
            .build()

        assertEquals(Query("SELECT * FROM people WHERE true LIMIT 34", emptyList()), query)
    }

    @Test
    fun `use offset to skip N rows`() {
        val query = QueryBuilderSql()
            .from("people")
            .limit(34, 6)
            .build()

        assertEquals(Query("SELECT * FROM people WHERE true LIMIT 34 OFFSET 6", emptyList()), query)
    }
}