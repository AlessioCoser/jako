package dbhelper.query

import dbhelper.query.fields.Fields.AS
import dbhelper.query.fields.Fields.wrap
import dbhelper.query.conditions.And
import dbhelper.query.conditions.Eq
import dbhelper.query.conditions.Gt
import dbhelper.query.fields.Aggregates.AVG
import dbhelper.query.fields.Aggregates.COUNT
import dbhelper.query.fields.Aggregates.EVERY
import dbhelper.query.fields.Aggregates.MAX
import dbhelper.query.fields.Aggregates.MIN
import dbhelper.query.fields.Aggregates.SUM
import dbhelper.query.join.On
import dbhelper.query.join.On.Companion.EQ
import dbhelper.query.join.On.Companion.ON
import dbhelper.query.order.Asc
import dbhelper.query.order.Desc
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
    fun `build query choosing fields to select`() {
        val query = QueryBuilder()
            .from("people")
            .fields("first", "second")
            .build()

        assertEquals(Query("""SELECT "first", "second" FROM "people"""", emptyList()), query)
    }

    @Test
    fun `add order by asc clause`() {
        val query = QueryBuilder()
            .from("people")
            .orderBy(Asc("first", "second"))
            .build()

        assertEquals(Query("""SELECT * FROM "people" ORDER BY "first" ASC, "second" ASC""", emptyList()), query)
    }

    @Test
    fun `add order by with one asc and one desc clause`() {
        val query = QueryBuilder()
            .from("people")
            .orderBy(Asc("first"), Desc("second"))
            .build()

        assertEquals(Query("""SELECT * FROM "people" ORDER BY "first" ASC, "second" DESC""", emptyList()), query)
    }

    @Test
    fun `single limits to one row`() {
        val query = QueryBuilder()
            .from("people")
            .single()
            .build()

        assertEquals(Query("SELECT * FROM \"people\" LIMIT 1", emptyList()), query)
    }

    @Test
    fun `limit clause`() {
        val query = QueryBuilder()
            .from("people")
            .limit(34)
            .build()

        assertEquals(Query("""SELECT * FROM "people" LIMIT 34""", emptyList()), query)
    }

    @Test
    fun `use offset to skip N rows`() {
        val query = QueryBuilder()
            .from("people")
            .limit(34, 6)
            .build()

        assertEquals(Query("SELECT * FROM \"people\" LIMIT 34 OFFSET 6", emptyList()), query)
    }

    @Test
    fun `group by`() {
        val query = QueryBuilder()
            .from("people")
            .fields("name", "count(name)" AS "total")
            .groupBy("name")
            .build()

        assertEquals(Query("""SELECT "name", count("name") AS "total" FROM "people" GROUP BY "name"""", emptyList()), query)
    }

    @Test
    fun `group by with having`() {
        val query = QueryBuilder()
            .from("people")
            .fields("name", "count(name)")
            .groupBy("name")
            .having(Gt("count(name)", 20))
            .build()

        assertEquals(
            Query("""SELECT "name", count("name") FROM "people" GROUP BY "name" HAVING count("name") > ?""", listOf(20)),
            query
        )
    }

    @Test
    fun `group by with having sum`() {
        val query = QueryBuilder()
            .from("people")
            .fields("name", SUM("name"))
            .groupBy("name")
            .having(Gt(SUM("name"), 20))
            .build()

        assertEquals(
            Query("""SELECT "name", sum("name") FROM "people" GROUP BY "name" HAVING sum("name") > ?""", listOf(20)),
            query
        )
    }

    @Test
    fun `group by with having min`() {
        val query = QueryBuilder()
            .from("people")
            .fields("name", MIN("name"))
            .groupBy("name")
            .having(Gt(MIN("name"), 20))
            .build()

        assertEquals(
            Query("""SELECT "name", min("name") FROM "people" GROUP BY "name" HAVING min("name") > ?""", listOf(20)),
            query
        )
    }

    @Test
    fun `group by with having max`() {
        val query = QueryBuilder()
            .from("people")
            .fields("name", MAX("name"))
            .groupBy("name")
            .having(Gt(MAX("name"), 20))
            .build()

        assertEquals(
            Query("""SELECT "name", max("name") FROM "people" GROUP BY "name" HAVING max("name") > ?""", listOf(20)),
            query
        )
    }

    @Test
    fun `group by with having every`() {
        val query = QueryBuilder()
            .from("people")
            .fields("name", EVERY("name"))
            .groupBy("name")
            .having(Gt(EVERY("name"), 20))
            .build()

        assertEquals(
            Query("""SELECT "name", every("name") FROM "people" GROUP BY "name" HAVING every("name") > ?""", listOf(20)),
            query
        )
    }

    @Test
    fun `group by with having avg`() {
        val query = QueryBuilder()
            .from("people")
            .fields("name", AVG("name"))
            .groupBy("name")
            .having(Gt(AVG("name"), 20))
            .build()

        assertEquals(
            Query("""SELECT "name", avg("name") FROM "people" GROUP BY "name" HAVING avg("name") > ?""", listOf(20)),
            query
        )
    }

    @Test
    fun `where statement`() {
        val query = QueryBuilder()
            .from("people")
            .fields("age")
            .fields("age")
            .where(Eq("age", 20))
            .build()

        assertEquals(Query("""SELECT "age" FROM "people" WHERE "age" = ?""", listOf(20)), query)
    }

    @Test
    fun `multiple where statement`() {
        val query = QueryBuilder()
            .from("people")
            .fields("age")
            .where(And(Eq("nationality", "Italian"), Gt("age", 20)))
            .build()

        assertEquals(Query("""SELECT "age" FROM "people" WHERE ("nationality" = ? AND "age" > ?)""", listOf("Italian", 20)), query)
    }

    @Test
    fun `multiple join statement`() {
        val query = QueryBuilder()
            .from("people")
            .join(On("bank_account", "people.id", "bank_account.person_id"))
            .leftJoin(On("pets", "people.id", "pets.owner"))
            .build()

        assertEquals(
            Query(
                "SELECT * FROM \"people\" " +
                        "INNER JOIN \"bank_account\" ON \"people\".\"id\" = \"bank_account\".\"person_id\" " +
                        "LEFT JOIN \"pets\" ON \"people\".\"id\" = \"pets\".\"owner\"", emptyList()
            ), query
        )
    }

    @Test
    fun `join statement with dsl syntax`() {
        val query = QueryBuilder()
            .from("people")
            .join("bank_account" ON "people.id" EQ "bank_account.person_id")
            .rightJoin("pets" ON "owner_id")
            .build()

        assertEquals(
            Query(
                "SELECT * FROM \"people\" " +
                        "INNER JOIN \"bank_account\" ON \"people\".\"id\" = \"bank_account\".\"person_id\" " +
                        "RIGHT JOIN \"pets\" USING(\"owner_id\")", emptyList()
            ), query
        )
    }

    @Test
    fun `all together in right order`() {
        val query = QueryBuilder()
            .from("people")
            .fields("name", COUNT("*") AS "total")
            .where(And(Eq("nationality", "Italian"), Gt("age", 20)))
            .join(On("bank_account", "people.id", "bank_account.person_id"))
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
                        """WHERE ("nationality" = ? AND "age" > ?) """ +
                        """GROUP BY "name" """ +
                        """HAVING count(*) > ? """ +
                        """ORDER BY "first" ASC, "second" ASC """ +
                        """LIMIT 34 OFFSET 6""", listOf("Italian", 20, 12)
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