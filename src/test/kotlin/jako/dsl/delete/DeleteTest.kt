package jako.dsl.delete

import jako.dsl.conditions.EQ
import jako.dsl.conditions.GT
import jako.dsl.fields.col
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class DeleteTest {
    @Test
    fun `delete with where statement`() {
        val insert = Delete
            .from("users")
            .where("id" EQ 1)

        println(insert.toString())
        // DELETE FROM "users" WHERE "id" = ?
        println(insert.params())
        // [1]
    }

    @Test
    fun `update without from`() {
        val message = assertThrows(RuntimeException::class.java) {
            Delete().toString()
        }.message

        assertThat(message).isEqualTo("Cannot generate delete without table name")
    }

    @Test
    fun `delete with where statement `() {
        val insert = Delete()
            .from("table")
            .where("column1" GT 10)

        assertEquals("""DELETE FROM "table" WHERE "column1" > ?""", insert.toString())
        assertEquals(listOf(10), insert.params())
    }

    @Test
    fun `Delete with returning Field`() {
        val insert = Delete().from("table")
            .returning("id".col, "name".col)

        assertEquals("""DELETE FROM "table" RETURNING "id", "name"""", insert.toString())
    }

    @Test
    fun `Delete with returning String`() {
        val insert = Delete().from("table")
            .returning("id", "name")

        assertEquals("""DELETE FROM "table" RETURNING "id", "name"""", insert.toString())
    }
}
