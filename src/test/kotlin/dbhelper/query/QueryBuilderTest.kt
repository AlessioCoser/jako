package dbhelper.query

import dbhelper.query.conditions.And
import dbhelper.query.conditions.Eq
import dbhelper.query.conditions.Gt
import dbhelper.query.fields.Fields.Companion.AS
import dbhelper.query.fields.Fields.Companion.COUNT
import dbhelper.query.join.On
import dbhelper.query.order.Asc
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class QueryBuilderTest {
    @Test
    fun `cannot build query without table name`() {
        val message = assertThrows(RuntimeException::class.java) {
            QueryBuilder().build()
        }.message

        assertEquals("Cannot generate query without table name", message)
    }

    @Test
    fun `build simple query`() {
        val query = QueryBuilder().from("people").build()

        assertEquals(Query("SELECT * FROM \"people\"", emptyList()), query)
    }

    @Test
    fun `build single query`() {
        val query = QueryBuilder().from("people").single().build()

        assertEquals(Query("SELECT * FROM \"people\" LIMIT 1", emptyList()), query)
    }

    @Test
    fun `all together in right order`() {
        val query = QueryBuilder()
            .from("people")
            .fields("name", COUNT("*") AS "total")
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
            Query(
                """SELECT "name", count(*) AS "total" """ +
                        """FROM "people" """ +
                        """INNER JOIN "bank_account" ON "people"."id" = "bank_account"."person_id" """ +
                        """LEFT JOIN "left" USING("identifier") """ +
                        """RIGHT JOIN "right" ON "people"."id" = "bank_account"."person_id" """ +
                        """WHERE "nationality" = ? """ +
                        """GROUP BY "name" """ +
                        """HAVING count(*) > ? """ +
                        """ORDER BY "first" ASC, "second" ASC """ +
                        """LIMIT 34 OFFSET 6""", listOf("Italian", 12)
            ), query
        )
    }

    @Test
    fun `raw overwrites all statements before used`() {
        val query = QueryBuilder()
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

        assertEquals(Query("SELECT * FROM customers", emptyList()), query)
    }

    @Test
    fun `raw can be used alone`() {
        val query = QueryBuilder()
            .raw("SELECT * FROM customers")
            .build()

        assertEquals(Query("SELECT * FROM customers", emptyList()), query)
    }

    @Test
    fun `raw can be used with some parameters`() {
        val query = QueryBuilder()
            .raw("SELECT * FROM customers WHERE age < ? AND age > ?", 20, 30)
            .build()

        assertEquals(Query("SELECT * FROM customers WHERE age < ? AND age > ?", listOf(20, 30)), query)
    }

    @Test
    fun `raw can be used statically`() {
        val query = QueryBuilder.raw("SELECT * FROM customers WHERE age < ? AND age > ?", 20, 30)

        assertEquals(Query("SELECT * FROM customers WHERE age < ? AND age > ?", listOf(20, 30)), query)
    }
}