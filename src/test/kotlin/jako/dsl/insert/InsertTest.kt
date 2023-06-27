package jako.dsl.insert

import jako.dsl.Dialect.All.PSQL
import jako.dsl.fields.col
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.sql.Date
import java.time.LocalDate

class InsertTest {
    @Test
    fun `insert into single param foreach column`() {
        val insert = Insert.into("table").set("col1", "1").set("col2", 2).set("col3", "3")

        assertEquals("INSERT INTO \"table\" (\"col1\", \"col2\", \"col3\") VALUES (?, ?, ?)", insert.toSQL(PSQL))
        assertEquals(listOf("1", 2, "3"), insert.params())
    }

    @Test
    fun `insert into EMPTY param`() {
        val insert = Insert.into("table").set("", "0").set("col1", "1")

        assertEquals("INSERT INTO \"table\" (\"col1\") VALUES (?)", insert.toSQL(PSQL))
        assertEquals(listOf("1"), insert.params())
    }

    @Test
    fun `insert into with empty table`() {
        val message = assertThrows(RuntimeException::class.java) {
            Insert.into("").set("col1", "1").toSQL(PSQL)
        }.message

        assertThat(message).isEqualTo("Cannot generate insert without table name")
    }

    @Test
    fun `insert without values`() {
        val message = assertThrows(RuntimeException::class.java) {
            Insert().into("table").toSQL(PSQL)
        }.message

        assertThat(message).isEqualTo("Cannot generate insert without values")
    }

    @Test
    fun `insert local-date`() {
        val insert = Insert()
            .into("table")
            .set("column1", LocalDate.of(2022, 4, 1))

        assertEquals("""INSERT INTO "table" ("column1") VALUES (?)""", insert.toSQL(PSQL))
        assertEquals(listOf(Date.valueOf("2022-04-01")), insert.params())
    }

    @Test
    fun `insert with returning Field`() {
        val insert = Insert()
            .into("table")
            .set("column1", "value1")
            .returning("id".col, "name".col)

        assertEquals("""INSERT INTO "table" ("column1") VALUES (?) RETURNING "id", "name"""", insert.toSQL(PSQL))
        assertEquals(listOf("value1"), insert.params())
    }

    @Test
    fun `insert with returning String`() {
        val insert = Insert()
            .into("table")
            .set("column1", "value1")
            .returning("id", "name")

        assertEquals("""INSERT INTO "table" ("column1") VALUES (?) RETURNING "id", "name"""", insert.toSQL(PSQL))
        assertEquals(listOf("value1"), insert.params())
    }

    @Test
    fun `ignore not present fields`() {
        val insert = Insert()
            .into("table")
            .set("column1", "value1")
            .returning("", "name")

        assertEquals("""INSERT INTO "table" ("column1") VALUES (?) RETURNING "name"""", insert.toSQL(PSQL))
        assertEquals(listOf("value1"), insert.params())
    }

    @Test
    fun `ignore returning without fields`() {
        val insert = Insert()
            .into("table")
            .set("column1", "value1")
            .returning("")

        assertEquals("""INSERT INTO "table" ("column1") VALUES (?)""", insert.toSQL(PSQL))
        assertEquals(listOf("value1"), insert.params())
    }
}