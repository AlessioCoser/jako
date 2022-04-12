package dbhelper.dsl.insert

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.sql.Date
import java.time.LocalDate

class InsertBuilderTest {
    @Test
    fun `insert into single param foreach column`() {
        val insert = dbhelper.dsl.insert.InsertBuilder()
            .into("table").set("column1", "1").set("column2", 2).set("column3", "3").build()

        assertEquals("INSERT INTO \"table\" (\"column1\", \"column2\", \"column3\") VALUES (?, ?, ?)", insert.statement)
        assertEquals(listOf("1", 2, "3"), insert.params)
    }

    @Test
    fun `insert raw statement`() {
        val insert = dbhelper.dsl.insert.InsertBuilder()
            .raw("INSERT INTO table (column1, column2) VALUES (?, ?)", "1", 2).build()

        assertEquals("INSERT INTO table (column1, column2) VALUES (?, ?)", insert.statement)
        assertEquals(listOf("1", 2), insert.params)
    }

    @Test
    fun `insert raw wins over other insert`() {
        val insert = dbhelper.dsl.insert.InsertBuilder()
            .into("table")
            .set("column1", "1")
            .raw("INSERT INTO table (column1, column2) VALUES (?, ?)", "1", 2)
            .build()

        assertEquals("INSERT INTO table (column1, column2) VALUES (?, ?)", insert.statement)
        assertEquals(listOf("1", 2), insert.params)
    }

    @Test
    fun `insert raw statement statically`() {
        val insert = dbhelper.dsl.insert.InsertBuilder.raw("INSERT INTO table (column1, column2) VALUES (?, ?)", "1", 2)

        assertEquals("INSERT INTO table (column1, column2) VALUES (?, ?)", insert.statement)
        assertEquals(listOf("1", 2), insert.params)
    }

    @Test
    fun `insert without values`() {
        val message = assertThrows(RuntimeException::class.java) {
            dbhelper.dsl.insert.InsertBuilder().into("table").build()
        }.message

        assertThat(message).isEqualTo("Cannot generate insert without values")
    }

    @Test
    fun `insert local-date`() {
        val insert = dbhelper.dsl.insert.InsertBuilder()
            .into("table")
            .set("column1", LocalDate.of(2022, 4, 1))
            .build()

        assertEquals("""INSERT INTO "table" ("column1") VALUES (?)""", insert.statement)
        assertEquals(listOf(Date.valueOf("2022-04-01")), insert.params)
    }
}