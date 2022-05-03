package dbhelper.dsl.query

import dbhelper.dsl.Statement
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
            Query().build()
        }.message

        assertEquals("Cannot generate query without table name", message)
    }

    @Test
    fun `build simple query`() {
        val query = Query().from("people").build()

        assertEquals(Statement("SELECT * FROM \"people\"", emptyList()), query)
    }

    @Test
    fun `build single query`() {
        val query = Query().from("people").single().build()

        assertEquals(Statement("SELECT * FROM \"people\" LIMIT 1", emptyList()), query)
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
            .build()

        assertEquals(
            Statement(
                """SELECT "name", COUNT(*) AS "total", COALESCE(MAX("age"), ?) - 1 """ +
                        """FROM "people" """ +
                        """INNER JOIN "bank_account" ON "people"."id" = "bank_account"."person_id" """ +
                        """LEFT JOIN "left" USING("identifier") """ +
                        """RIGHT JOIN "right" ON "people"."id" = "bank_account"."person_id" """ +
                        """WHERE "nationality" = ? """ +
                        """GROUP BY "name" """ +
                        """HAVING COUNT(*) > ? """ +
                        """ORDER BY "first" ASC, "second" ASC """ +
                        """LIMIT 34 OFFSET 6""", listOf(1, "Italian", 12)
            ), query
        )
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
            .build()

        assertEquals(Statement("SELECT * FROM customers", emptyList()), query)
    }

    @Test
    fun `raw can be used alone`() {
        val query = Query()
            .raw("SELECT * FROM customers")
            .build()

        assertEquals(Statement("SELECT * FROM customers", emptyList()), query)
    }

    @Test
    fun `raw can be used with some parameters`() {
        val query = Query()
            .raw("SELECT * FROM customers WHERE age < ? AND age > ?", 20, 30)
            .build()

        assertEquals(Statement("SELECT * FROM customers WHERE age < ? AND age > ?", listOf(20, 30)), query)
    }

    @Test
    fun `raw can be used statically`() {
        val query = Query.raw("SELECT * FROM customers WHERE age < ? AND age > ?", 20, 30)

        assertEquals(Statement("SELECT * FROM customers WHERE age < ? AND age > ?", listOf(20, 30)), query)
    }
}