package jako.dsl.query

import jako.dsl.Dialect.All.PSQL
import jako.dsl.conditions.*
import jako.dsl.fields.AS
import jako.dsl.fields.col
import jako.dsl.fields.functions.*
import jako.dsl.query.join.On
import jako.dsl.query.order.Asc
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class QueryTest {
    @Test
    fun `cannot build query without table name`() {
        val message = assertThrows(RuntimeException::class.java) {
            Query().toSQL(PSQL)
        }.message

        assertEquals("Cannot generate query without table name", message)
    }

    @Test
    fun `build simple query`() {
        val query = Query.from("people")

        assertEquals("SELECT * FROM \"people\"", query.toSQL(PSQL))
        assertEquals(emptyList<Any?>(), query.params())
    }

    @Test
    fun `build single query`() {
        val query = Query().from("people").single()

        assertEquals("SELECT * FROM \"people\" LIMIT 1", query.toSQL(PSQL))
        assertEquals(emptyList<Any?>(), query.params())
    }

    @Test
    fun `build single query with empty where`() {
        val query = Query().from("people").where(("" IS null) AND ("" NOT null)).single()

        assertEquals("SELECT * FROM \"people\" LIMIT 1", query.toSQL(PSQL))
        assertEquals(emptyList<Any?>(), query.params())
    }

    @Test
    fun `build single query with partially AND in where`() {
        val query = Query().from("people").where(And(Eq("", 1), Eq("test", 2))).single()

        assertEquals("SELECT * FROM \"people\" WHERE (\"test\" = ?) LIMIT 1", query.toSQL(PSQL))
        assertEquals(listOf<Any?>(2), query.params())
    }

    @Test
    fun `build single query with partially OR in where`() {
        val query = Query().from("people").where(("" EQ 1) OR ("test" EQ 2)).single()

        assertEquals("SELECT * FROM \"people\" WHERE (\"test\" = ?) LIMIT 1", query.toSQL(PSQL))
        assertEquals(listOf<Any?>(2), query.params())
    }

    @Test
    fun `build single query with partially OR in having`() {
        val query = Query().from("people").having("" EQ 1).single()

        assertEquals("SELECT * FROM \"people\" LIMIT 1", query.toSQL(PSQL))
        assertEquals(emptyList<Any?>(), query.params())
    }

    @Test
    fun `build single query with empty Fields as string`() {
        val query = Query().from("people").fields("").single()

        assertEquals("SELECT * FROM \"people\" LIMIT 1", query.toSQL(PSQL))
        assertEquals(emptyList<Any?>(), query.params())
    }

    @Test
    fun `build single query with empty function Fields`() {
        val query = Query().from("people").fields(AVG(""), EVERY(""), SUM(""), MIN("") + 1).single()

        assertEquals("SELECT * FROM \"people\" LIMIT 1", query.toSQL(PSQL))
        assertEquals(emptyList<Any?>(), query.params())
    }

    @Test
    fun `build empty query`() {
        val query = Query().from("people")

        assertEquals("SELECT * FROM \"people\"", query.toSQL(PSQL))
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
                """LIMIT 34 OFFSET 6""", query.toSQL(PSQL))
        assertEquals(listOf(1, "Italian", 12), query.params())
    }
}