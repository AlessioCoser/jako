package jako.dsl

import jako.dsl.Dialect.All.PSQL
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class RawStatementTest {
    @Test
    fun `raw statement`() {
        val insert = RawStatement("INSERT INTO table (column1, column2) VALUES (?, ?)", listOf("1", 2))

        assertEquals("INSERT INTO table (column1, column2) VALUES (?, ?)", insert.toSQL(PSQL))
        assertEquals(listOf("1", 2), insert.params())
    }

    @Test
    fun `empty statement should not be present`() {
        val query = RawStatement("")

        assertFalse(query.isPresent())
        assertEquals(emptyList<Any?>(), query.params())
    }

// TODO: raw statement and statement builder

}