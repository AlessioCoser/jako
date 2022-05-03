# JAKO: Just Another Kotlin Orm (PostgreSQL)
![Daikon](./jako.png)

**All the examples are written as tests using JUnit5.**

## How to build a simple select statement
```kotlin
import jako.dsl.conditions.Eq.Companion.EQ
import jako.dsl.query.Query
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Docs {
    @Test fun `build a select statement with a simple condition`() {
        val statement = Query().from("users").where("city" EQ "Milano")

        assertEquals("""SELECT * FROM "users" WHERE "city" = ?""", statement.toString())
        assertEquals(listOf("Milano"), statement.params())
    }
}
```

## How to build a simple select statement
In order to run this test you have to start a postgresql server to the "localhost:5432" with the necessary data.
```kotlin
import jako.database.Database
import jako.dsl.query.Query
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Docs {
    @Test fun `select all ids of table`() {
        val db = Database.connect("jdbc:postgresql://localhost:5432/tests?user=user&password=password")
        val tableIds = db.select(Query().from("table")).all { int("id") }

        assertEquals(listOf(1, 2, 3), tableIds)
    }
}
```