package dbhelper.dsl.query

import dbhelper.dsl.conditions.And
import dbhelper.dsl.conditions.Eq
import dbhelper.dsl.conditions.Gt
import dbhelper.dsl.fields.As.Companion.AS
import dbhelper.dsl.fields.Column.Companion.col
import dbhelper.dsl.fields.functions.Coalesce.Companion.COALESCE
import dbhelper.dsl.fields.functions.Count.Companion.COUNT
import dbhelper.dsl.fields.functions.Max.Companion.MAX
import dbhelper.dsl.query.join.On
import dbhelper.dsl.query.order.Asc
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class QueryTest {
    @Test
    fun `cannot build query without table name`() {
        val message = assertThrows(RuntimeException::class.java) {
            Query().toString()
        }.message

        assertEquals("Cannot generate query without table name", message)
    }

    @Test
    fun `build simple query`() {
        val query = Query().from("people")

        assertEquals("SELECT * FROM \"people\"", query.toString())
        assertEquals(emptyList<Any?>(), query.params())
    }

    @Test
    fun `build single query`() {
        val query = Query().from("people").single()

        assertEquals("SELECT * FROM \"people\" LIMIT 1", query.toString())
        assertEquals(emptyList<Any?>(), query.params())
    }

    @Test
    fun `all together in right order`() {
        val query = Query()
            .from("people")
            .fields("name".col, COUNT("*") AS "total", COALESCE(MAX("age"), 1) - 1)
            .where(Eq("nationality", "Italian"))
            .join(On("bank_account", "people.id", "bank_account.person_id"))
            .leftJoin(On("left", "identifier"))
            .rightJoin(On("right", "people.id", "bank_account.person_id"))
            .groupBy("name")
            .having(Gt(COUNT("*"), 12))
            .orderBy(Asc("first", "second"))
            .limit(34, 6)

        assertEquals("""SELECT "name", COUNT(*) AS "total", COALESCE(MAX("age"), ?) - 1 """ +
                """FROM "people" """ +
                """INNER JOIN "bank_account" ON "people"."id" = "bank_account"."person_id" """ +
                """LEFT JOIN "left" USING("identifier") """ +
                """RIGHT JOIN "right" ON "people"."id" = "bank_account"."person_id" """ +
                """WHERE "nationality" = ? """ +
                """GROUP BY "name" """ +
                """HAVING COUNT(*) > ? """ +
                """ORDER BY "first" ASC, "second" ASC """ +
                """LIMIT 34 OFFSET 6""", query.toString())
        assertEquals(listOf(1, "Italian", 12), query.params())
    }

    @Test
    fun `raw overwrites all statements before used`() {
        val query = Query()
            .from("people")
            .fields("name", "count(name) AS total")
            .where(And(Eq("nationality", "Italian"), Gt("age", 20)))
            .join(On("bank_account", "people.id", "bank_account.person_id"))
            .groupBy("name")
            .having(Gt("count(name)", 12))
            .orderBy(Asc("first", "second"))
            .limit(34, 6)
            .raw("SELECT * FROM customers")

        assertEquals("SELECT * FROM customers", query.toString())
        assertEquals(emptyList<Any?>(), query.params())
    }

    @Test
    fun `raw can be used alone`() {
        val query = Query()
            .raw("SELECT * FROM customers")

        assertEquals("SELECT * FROM customers", query.toString())
        assertEquals(emptyList<Any?>(), query.params())
    }

    @Test
    fun `raw can be used with some parameters`() {
        val query = Query()
            .raw("SELECT * FROM customers WHERE age < ? AND age > ?", 20, 30)

        assertEquals("SELECT * FROM customers WHERE age < ? AND age > ?", query.toString())
        assertEquals(listOf(20, 30), query.params())
    }

    @Test
    fun `raw can be used statically`() {
        val query = Query().raw("SELECT * FROM customers WHERE age < ? AND age > ?", 20, 30)

        assertEquals("SELECT * FROM customers WHERE age < ? AND age > ?", query.toString())
        assertEquals(listOf(20, 30), query.params())
    }
}