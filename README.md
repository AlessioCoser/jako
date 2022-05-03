# JAKO: Just Another Kotlin Orm
It's a simple Kotlin Orm for PostgreSQL.
It's made of 2 parts:
- statement builders
- statement executors


## Statement Builders
Classes: Query, Insert, Update

```kotlin
import jako.dsl.conditions.Eq.Companion.EQ
import jako.dsl.query.Query
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Docs {
    @Test fun `build a select statement with a condition`() {
        val statement = Query().from("users").where("city" EQ "Milano")

        assertEquals("""SELECT * FROM "users" WHERE "city" = ?""", statement.toString())
        assertEquals(listOf("Milano"), statement.params())
    }
}
```