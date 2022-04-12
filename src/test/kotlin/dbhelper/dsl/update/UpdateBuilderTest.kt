package dbhelper.dsl.update

import dbhelper.dsl.conditions.Eq.Companion.EQ
import dbhelper.dsl.conditions.Gt.Companion.GT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.sql.Date
import java.time.LocalDate

class UpdateBuilderTest {
    @Test
    fun `update into single param foreach column`() {
        val insert = UpdateBuilder()
            .from("table").set("column1", "1").set("column2", 2).set("column3", "3").build()

        assertEquals("UPDATE \"table\" SET \"column1\" = ?, \"column2\" = ?, \"column3\" = ?", insert.statement)
        assertEquals(listOf("1", 2, "3"), insert.params)
    }

    @Test
    fun `update raw statement`() {
        val insert = UpdateBuilder()
            .raw("UPDATE table SET column1 = ?, column2 = ?", "1", 2).build()

        assertEquals("UPDATE table SET column1 = ?, column2 = ?", insert.statement)
        assertEquals(listOf("1", 2), insert.params)
    }

    @Test
    fun `update raw wins over other insert`() {
        val insert = UpdateBuilder()
            .from("table")
            .set("column1", "1")
            .raw("UPDATE table SET column1 = ?, column2 = ?", "1", 2)
            .build()

        assertEquals("UPDATE table SET column1 = ?, column2 = ?", insert.statement)
        assertEquals(listOf("1", 2), insert.params)
    }

    @Test
    fun `update raw statement statically`() {
        val insert = UpdateBuilder.raw("UPDATE table SET column1 = ?, column2 = ?", "1", 2)

        assertEquals("UPDATE table SET column1 = ?, column2 = ?", insert.statement)
        assertEquals(listOf("1", 2), insert.params)
    }

    @Test
    fun `update without values`() {
        val message = assertThrows(RuntimeException::class.java) {
            UpdateBuilder().from("table").build()
        }.message

        assertThat(message).isEqualTo("Cannot generate update without values")
    }

    @Test
    fun `update local-date`() {
        val insert = UpdateBuilder()
            .from("table")
            .set("column1", LocalDate.of(2022, 4, 1))
            .build()

        assertEquals("""UPDATE "table" SET "column1" = ?""", insert.statement)
        assertEquals(listOf(Date.valueOf("2022-04-01")), insert.params)
    }

    @Test
    fun `update with where statement `() {
        val insert = UpdateBuilder()
            .from("table")
            .set("column1", 0)
            .where("column1" GT 10)
            .build()

        assertEquals("""UPDATE "table" SET "column1" = ? WHERE "column1" > ?""", insert.statement)
        assertEquals(listOf(0, 10), insert.params)
    }
}
