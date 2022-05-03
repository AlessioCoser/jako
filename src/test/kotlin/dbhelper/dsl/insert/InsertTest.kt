package dbhelper.dsl.insert

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.sql.Date
import java.time.LocalDate

class InsertTest {
    @Test
    fun `insert into single param foreach column`() {
        val insert = Insert()
            .into("table").set("col1", "1").set("col2", 2).set("col3", "3")

        assertEquals("INSERT INTO \"table\" (\"col1\", \"col2\", \"col3\") VALUES (?, ?, ?)", insert.toString())
        assertEquals(listOf("1", 2, "3"), insert.params())
    }

    @Test
    fun `insert raw statement`() {
        val insert = Insert()
            .raw("INSERT INTO table (column1, column2) VALUES (?, ?)", "1", 2)

        assertEquals("INSERT INTO table (column1, column2) VALUES (?, ?)", insert.toString())
        assertEquals(listOf("1", 2), insert.params())
    }

    @Test
    fun `insert raw wins over other insert`() {
        val insert = Insert()
            .into("table")
            .set("column1", "1")
            .raw("INSERT INTO table (column1, column2) VALUES (?, ?)", "1", 2)

        assertEquals("INSERT INTO table (column1, column2) VALUES (?, ?)", insert.toString())
        assertEquals(listOf("1", 2), insert.params())
    }

    @Test
    fun `insert raw statement statically`() {
        val insert = Insert().raw("INSERT INTO table (column1, column2) VALUES (?, ?)", "1", 2)

        assertEquals("INSERT INTO table (column1, column2) VALUES (?, ?)", insert.toString())
        assertEquals(listOf("1", 2), insert.params())
    }

    @Test
    fun `insert without values`() {
        val message = assertThrows(RuntimeException::class.java) {
            Insert().into("table").toString()
        }.message

        assertThat(message).isEqualTo("Cannot generate insert without values")
    }

    @Test
    fun `insert local-date`() {
        val insert = Insert()
            .into("table")
            .set("column1", LocalDate.of(2022, 4, 1))

        assertEquals("""INSERT INTO "table" ("column1") VALUES (?)""", insert.toString())
        assertEquals(listOf(Date.valueOf("2022-04-01")), insert.params())
    }
}