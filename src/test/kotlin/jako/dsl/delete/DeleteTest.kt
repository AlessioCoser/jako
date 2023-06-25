package jako.dsl.delete

import jako.dsl.Dialect.All.MYSQL
import jako.dsl.Dialect.All.PSQL
import jako.dsl.conditions.GT
import jako.dsl.fields.col
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class DeleteTest {
    @Test
    fun `update without from`() {
        val message = assertThrows(RuntimeException::class.java) {
            Delete().toSQL(PSQL)
        }.message

        assertThat(message).isEqualTo("Cannot generate delete without table name")
    }

    @Test
    fun `delete with where statement `() {
        val insert = Delete()
            .from("table")
            .where("column1" GT 10)

        assertEquals("""DELETE FROM "table" WHERE "column1" > ?""", insert.toSQL(PSQL))
        assertEquals("""DELETE FROM `table` WHERE `column1` > ?""", insert.toSQL(MYSQL))
        assertEquals(listOf(10), insert.params())
    }

    @Test
    fun `Delete with returning Field`() {
        val insert = Delete().from("table")
            .returning("id".col, "name".col)

        assertEquals("""DELETE FROM "table" RETURNING "id", "name"""", insert.toSQL(PSQL))
    }

    @Test
    fun `Delete with returning String`() {
        val insert = Delete().from("table")
            .returning("id", "name")

        assertEquals("""DELETE FROM "table" RETURNING "id", "name"""", insert.toSQL(PSQL))
    }
}
