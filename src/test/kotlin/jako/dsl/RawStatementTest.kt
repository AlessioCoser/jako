package jako.dsl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RawStatementTest {
    @Test
    fun `raw statement`() {
        val insert = RawStatement("INSERT INTO table (column1, column2) VALUES (?, ?)", listOf("1", 2))

        Assertions.assertEquals("INSERT INTO table (column1, column2) VALUES (?, ?)", insert.toString())
        Assertions.assertEquals(listOf("1", 2), insert.params())
    }
}