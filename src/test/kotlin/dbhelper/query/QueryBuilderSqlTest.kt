package dbhelper.query

import dbhelper.query.conditions.And
import dbhelper.query.conditions.Eq
import dbhelper.query.conditions.Gt
import dbhelper.query.join.InnerJoin
import dbhelper.query.join.LeftJoin
import dbhelper.query.join.RightJoin
import dbhelper.query.order.Asc
import dbhelper.query.order.Desc
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class QueryBuilderSqlTest {
    @Test
    fun `cannot build query without table name`() {
        val message = assertThrows(RuntimeException::class.java) {
            QueryBuilderSql().build()
        }.message

        assertEquals("Cannot generate query without table name", message)
    }

    @Test
    fun `build simple query`() {
        val query = QueryBuilderSql().from("people").build()

        assertEquals(Query("SELECT * FROM people WHERE true", emptyList()), query)
    }

    @Test
    fun `build query choosing fields to select`() {
        val query = QueryBuilderSql()
            .from("people")
            .fields("first", "second")
            .build()

        assertEquals(Query("SELECT first, second FROM people WHERE true", emptyList()), query)
    }

    @Test
    fun `add order by asc clause`() {
        val query = QueryBuilderSql()
            .from("people")
            .orderBy(Asc("first", "second"))
            .build()

        assertEquals(Query("SELECT * FROM people WHERE true ORDER BY first ASC, second ASC", emptyList()), query)
    }

    @Test
    fun `add order by with one asc and one desc clause`() {
        val query = QueryBuilderSql()
            .from("people")
            .orderBy(Asc("first"), Desc("second"))
            .build()

        assertEquals(Query("SELECT * FROM people WHERE true ORDER BY first ASC, second DESC", emptyList()), query)
    }

    @Test
    fun `single limits to one row`() {
        val query = QueryBuilderSql()
            .from("people")
            .single()
            .build()

        assertEquals(Query("SELECT * FROM people WHERE true LIMIT 1", emptyList()), query)
    }

    @Test
    fun `limit clause`() {
        val query = QueryBuilderSql()
            .from("people")
            .limit(34)
            .build()

        assertEquals(Query("SELECT * FROM people WHERE true LIMIT 34", emptyList()), query)
    }

    @Test
    fun `use offset to skip N rows`() {
        val query = QueryBuilderSql()
            .from("people")
            .limit(34, 6)
            .build()

        assertEquals(Query("SELECT * FROM people WHERE true LIMIT 34 OFFSET 6", emptyList()), query)
    }

    @Test
    fun `group by`() {
        val query = QueryBuilderSql()
            .from("people")
            .fields("name", "count(name) AS total")
            .groupBy("name")
            .build()

        assertEquals(Query("SELECT name, count(name) AS total FROM people WHERE true GROUP BY name", emptyList()), query)
    }

    @Test
    fun `group by with having`() {
        val query = QueryBuilderSql()
            .from("people")
            .fields("name", "count(name)")
            .groupBy("name")
            .having(Gt("count(name)", 20))
            .build()

        assertEquals(Query("SELECT name, count(name) FROM people WHERE true GROUP BY name HAVING count(name) > ?", listOf(20)), query)
    }

    @Test
    fun `where statement`() {
        val query = QueryBuilderSql()
            .from("people")
            .fields("age")
            .where(Eq("age", 20))
            .build()

        assertEquals(Query("SELECT age FROM people WHERE age = ?", listOf(20)), query)
    }

    @Test
    fun `multiple where statement`() {
        val query = QueryBuilderSql()
            .from("people")
            .fields("age")
            .where(And(Eq("nationality", "Italian"), Gt("age", 20)))
            .build()

        assertEquals(Query("SELECT age FROM people WHERE (nationality = ? AND age > ?)", listOf("Italian", 20)), query)
    }

    @Test
    fun `multiple join statement`() {
        val query = QueryBuilderSql()
            .from("people")
            .join(InnerJoin("bank_account", Eq("people.id", "bank_account.person_id")))
            .join(LeftJoin("pets", Eq("people.id", "pets.owner")))
            .build()

        assertEquals(Query("SELECT * FROM people " +
                "INNER JOIN bank_account ON people.id = bank_account.person_id " +
                "LEFT JOIN pets ON people.id = pets.owner " +
                "WHERE true", emptyList()), query)
    }

    @Test
    fun `all together in right order`() {
        val query = QueryBuilderSql()
            .from("people")
            .fields("name", "count(name) AS total")
            .where(And(Eq("nationality", "Italian"), Gt("age", 20)))
            .join(InnerJoin("bank_account", Eq("people.id", "bank_account.person_id")))
            .groupBy("name")
            .having(Gt("count(name)", 12))
            .orderBy(Asc("first", "second"))
            .limit(34, 6)
            .build()

        assertEquals(Query("SELECT name, count(name) AS total " +
                "FROM people " +
                "INNER JOIN bank_account ON people.id = bank_account.person_id " +
                "WHERE (nationality = ? AND age > ?) " +
                "GROUP BY name " +
                "HAVING count(name) > ? " +
                "ORDER BY first ASC, second ASC " +
                "LIMIT 34 OFFSET 6", listOf("Italian", 20, 12)
        ), query)
    }

    @Test
    fun `raw overwrites all statements before used`() {
        val query = QueryBuilderSql()
            .from("people")
            .fields("name", "count(name) AS total")
            .where(And(Eq("nationality", "Italian"), Gt("age", 20)))
            .join(InnerJoin("bank_account", Eq("people.id", "bank_account.person_id")))
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
        val query = QueryBuilderSql()
            .raw("SELECT * FROM customers")
            .build()

        assertEquals(Query("SELECT * FROM customers", emptyList()), query)
    }
}