package dbhelper.insert

import dbhelper.insert.Column.Companion.SET
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InsertBuilderTest {
    @Test
    fun `insert into statement and params all`() {
        val insert = InsertBuilder()
            .into("table")
            .values("column1" SET "1.1", "column2" SET "1.2", "column3" SET "1.3")
            .values("column1" SET "2.1", "column2" SET "2.2")
            .values("column1" SET "3.1", "column2" SET "3.2", "column3" SET "3.3")
            .values("column3" SET "4.3")
            .build()

        assertEquals(
            "INSERT INTO \"table\" (\"column1\", \"column2\", \"column3\") VALUES " +
                    "(?, ?, ?), " +
                    "(?, ?, ?), " +
                    "(?, ?, ?), " +
                    "(?, ?, ?)",
            insert.statement
        )
        assertEquals(
            listOf(
                "1.1", "1.2", "1.3",
                "2.1", "2.2", null,
                "3.1", "3.2", "3.3",
                null, null, "4.3"
            ), insert.params
        )
    }

    @Test
    fun `insert into single param foreach column`() {
        val insert = InsertBuilder().into("table").values("column1" SET "1", "column2" SET 2, "column3" SET "3").build()

        assertEquals("INSERT INTO \"table\" (\"column1\", \"column2\", \"column3\") VALUES (?, ?, ?)", insert.statement)
        assertEquals(listOf("1", 2, "3"), insert.params)
    }

    @Test
    fun `insert raw statement`() {
        val insert = InsertBuilder().raw("INSERT INTO table (column1, column2) VALUES (?, ?)", "1", 2).build()

        assertEquals("INSERT INTO table (column1, column2) VALUES (?, ?)", insert.statement)
        assertEquals(listOf("1", 2), insert.params)
    }

    @Test
    fun `insert raw wins over other insert`() {
        val insert = InsertBuilder()
            .into("table")
            .values("column1" SET "1")
            .raw("INSERT INTO table (column1, column2) VALUES (?, ?)", "1", 2)
            .build()

        assertEquals("INSERT INTO table (column1, column2) VALUES (?, ?)", insert.statement)
        assertEquals(listOf("1", 2), insert.params)
    }

    @Test
    fun `insert raw statement statically`() {
        val insert = InsertBuilder.raw("INSERT INTO table (column1, column2) VALUES (?, ?)", "1", 2)

        assertEquals("INSERT INTO table (column1, column2) VALUES (?, ?)", insert.statement)
        assertEquals(listOf("1", 2), insert.params)
    }
}