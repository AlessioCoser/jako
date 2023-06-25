# JAKO: Just Another Kotlin ORM
![JAKO](./jako.png)

JAKO is not actually a real ORM. it's a simple, minimal, no-dependency library to build and execute sql statements using a fluent syntax.
I currently tested it with postgresql 11 and mysql 8.

Main features:
- No dependencies
- Easy to use
- Statement builders totally independent of execution
- You can use your SQL query directly as String with value parameters
- Easy to create custom connectors (like HikariCP or others)
- No need to define table structures
- Fluent transactions

## Add JAKO to your project
[![](https://jitpack.io/v/AlessioCoser/jako.svg)](https://jitpack.io/#AlessioCoser/jako)

### Gradle
- Add JitPack in your root build.gradle at the end of repositories:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

- Add the dependency along with postgresql driver
```groovy
dependencies {
    implementation 'org.postgresql:postgresql:42.3.3'
    implementation 'com.github.AlessioCoser:jako:0.0.14'
}
```

- Or add the dependency along with mysql driver
```groovy
dependencies {
    implementation 'mysql:mysql-connector-java:8.0.15'
    implementation 'com.github.AlessioCoser:jako:0.0.14'
}
```

### Maven
- Add the JitPack repository to your build file
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
- Add the dependency along with postgresql driver
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.6.0</version>
</dependency>
<dependency>
    <groupId>com.github.AlessioCoser</groupId>
    <artifactId>jako</artifactId>
    <version>0.0.14</version>
</dependency>
```
- Or add the dependency along with mysql driver
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.15</version>
</dependency>
<dependency>
    <groupId>com.github.AlessioCoser</groupId>
    <artifactId>jako</artifactId>
    <version>0.0.14</version>
</dependency>
```

## Getting Started
### Build
#### Query
```kotlin
val query = Query.from("users")
    .join("pets" ON "pets.owner" EQ "users.id")
    .where(("city" EQ "Milano") AND ("age" GT 3))

println(query.toSQL(Dialect.All.PSQL))
// SELECT * FROM "users" INNER JOIN "pets" ON "pets"."owner" = "users"."id" WHERE ("city" = ? AND "age" > ?)
println(query.params())
// [Milano, 3]
```
#### Insert
```kotlin
val insert = Insert.into("users")
    .set("id", 1)
    .set("name", "Mario")
    .set("city", "Milano")
    .set("age", 30)

println(insert.toSQL(Dialect.All.PSQL))
// INSERT INTO "users" ("id", "name", "city", "age") VALUES (?, ?, ?, ?)
println(insert.params())
// [1, Mario, Milano, 30]
```
#### Update
```kotlin
val update = Update.table("users")
    .set("age", 31)
    .where("id" EQ 1)

println(update.toSQL(Dialect.All.PSQL))
// UPDATE "users" SET "age" = ? WHERE "id" = ?
println(update.params())
// [31, 1]
```
#### Delete
```kotlin
val delete = Delete.from("users")
    .where("id" EQ 1)

println(insert.toSQL(Dialect.All.PSQL))
// DELETE FROM "users" WHERE "id" = ?
println(insert.params())
// [1]
```

### Execute
#### Query
Select **all** `id` fields from `users` as Ints.

```kotlin
val db = Database.connect("jdbc:postgresql://localhost:5432/database?user=user&password=password", Dialect.All.PSQL)
val query = Query.from("users")

val tableIds: List<Int> = db.select(query).all { int("id") }
```
Select **first** `id` as Int from `users`.

```kotlin
val db = Database.connect("jdbc:postgresql://localhost:5432/database?user=user&password=password")
val query = Query.from("users")

val tableIds: Int? = db.select(query).first { int("id") }
```

Connect to **mysql**.

```kotlin
val db = Database.connect("jdbc:mysql://localhost:3306/database?user=user&password=password", Dialect.All.MYSQL)
val query = Query.from("users")

val tableIds: Int? = db.select(query).first { int("id") }
```

#### Another Statement
```kotlin
val db = Database.connect("jdbc:postgresql://localhost:5432/database?user=user&password=password", Dialect.All.PSQL)
val insert = Insert
    .into("customers")
    .set("name", "Carlo")
    .set("age", 18)

db.execute(insert)
```

#### Use your custom SQL string
Use your custom SQL string for SQL syntax not yet supported by the query builder.

```kotlin
val db = Database.connect("jdbc:postgresql://localhost:5432/database?user=user&password=password", Dialect.All.PSQL)

val tableIds: Int? = db.select("""SELECT "id" FROM "users" WHERE "city" = ?""", listOf("Milano")).first { int("id") }
```

## Execute statements in Transaction
Using `useTransaction` method you can run all db execute safely.
When something goes wrong and an execution throws an exception the changes are automatically rollbacked.
```kotlin
val db = Database.connect("jdbc:postgresql://localhost:5432/database?user=user&password=password", Dialect.All.PSQL)

db.useTransaction {
    db.execute(Insert.into("users").set("name", "Mario"))
    db.execute(Insert.into("users").set("name", "Paolo"))
    db.execute(Insert.into("users").set("name", "Carlo"))
}
```

## Print resulting query
#### Enable statement printing
```kotlin
val db = Database.connect("jdbc:postgresql://localhost:5432/database?user=user&password=password", Dialect.All.PSQL)
db.printStatements(true)
db.select(Query().from("users")).all { strOrNull("city") }
// prints to stdout:
// SELECT * FROM "users"
```

#### Change print destination
```kotlin
val connectionString = "jdbc:postgresql://localhost:5432/database?user=user&password=password"
val db = Database.connect(connectionString, Dialect.All.PSQL, System.out)
// System.out is the default, you can provide another implementation of PrintStream
```

## Custom Connectors
If you create a database instance without a custom connector the library use a SimpleConnector which adopt the standard `DriverManager.getConnection()` method to get a new database connection.

```kotlin
val db = Database.connect("jdbc:postgresql://localhost:5432/tests?user=user&password=password", Dialect.All.PSQL)
```

If you want to use this library in production we recommend to use a CustomConnector so you can use your connection pool/cache library.

So you have to create a database instance in this way:
```kotlin
val customConnector: DatabaseConnector = MyCustomConnector("jdbc:postgresql://localhost:5432/tests?user=user&password=password")
val db = Database.connect(customConnector, Dialect.All.PSQL)
```

In the example below we will create a Connector for [HikariCP](https://github.com/brettwooldridge/HikariCP)

### HikariCP Custom Connector Example
#### 1. Add HikariCP to the project dependencies
Add to dependencies:
```groovy
"com.zaxxer:HikariCP:4.0.3"
// for java 11 compatibility use the version 5.0.1
```

#### 2. Create the custom Connector

```kotlin
class HikariConnector(jdbcUrl: String, poolSize: Int = 10) : DatabaseConnector {
    private val dataSource = HikariDataSource().also {
        it.driverClassName = "org.postgresql.Driver"
        it.jdbcUrl = jdbcUrl
        it.maximumPoolSize = poolSize 
        // start with this: ((2 * core_count) + number_of_disks)
    }

    override fun connection(): Connection {
        return dataSource.connection
    }
}
```

#### 3. Use it
```kotlin
val customConnector: DatabaseConnector = HikariConnector("jdbc:postgresql://localhost:5432/tests?user=user&password=password")
val db = Database.connect(customConnector, Dialect.All.PSQL)
```
