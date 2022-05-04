# JAKO: Just Another Kotlin Orm (PostgreSQL)
![Daikon](./jako.png)

**All the examples are written as tests using JUnit5.**

## How to add JAKO to your project
[![](https://jitpack.io/v/AlessioCoser/jako.svg)](https://jitpack.io/#AlessioCoser/jako)

### Gradle
- Add JitPack in your root build.gradle at the end of repositories:
```
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```

- Add the dependency along with postgresql driver
```
implementation 'org.postgresql:postgresql:42.3.3'
implementation 'com.github.AlessioCoser:jako:0.0.4'
```

### Maven
- Add the JitPack repository to your build file
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
- Add the dependency along with postgresql driver
```
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.3.3</version>
</dependency>
<dependency>
    <groupId>com.github.AlessioCoser</groupId>
    <artifactId>jako</artifactId>
    <version>0.0.4</version>
</dependency>
```

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

## How to execute a simple select statement
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

## Custom Connectors
Create a database Instance:
```kotlin
val db = Database.connect("jdbc:postgresql://localhost:5432/tests?user=user&password=password")
```
Creating a database instance in the above way, the library use a SimpleConnector which adopt the standard `DriverManager.getConnection()` method to get a new database connection.

If you want to use this library in production we recommend to use a CustomConnector so you can use your connection pool library.

In the example below we will create a Connector for [HikariCP](https://github.com/brettwooldridge/HikariCP) 

### Example with HikariCP
#### 1. Add HikariCP to the project dependencies
Add to dependencies:
```
"com.zaxxer:HikariCP:4.0.3"
```
Or for Java11 compatibility
```
"com.zaxxer:HikariCP:5.0.1"
```
#### 2. Create the custom Connector
```kotlin
class HikariConnector(jdbcConnection: JdbcConnection, connectionPoolSize: Int = 10): DatabaseConnector {
    private val dataSource = HikariDataSource().also {
        it.driverClassName = "org.postgresql.Driver"
        it.jdbcUrl = jdbcConnection.connection
        it.maximumPoolSize = connectionPoolSize // start with this: ((2 * core_count) + number_of_disks)
    }

    override fun connection(): Connection {
        return dataSource.connection
    }
}
```

#### 3. Use it
```kotlin
val connectionConfig = JdbcConnection("jdbc:postgresql://localhost:5432/tests?user=user&password=password")
val db = Database.connect(HikariConnector(connectionConfig))
```