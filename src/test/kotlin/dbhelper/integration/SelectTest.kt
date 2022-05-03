package dbhelper.integration

import dbhelper.database.Database
import dbhelper.database.HikariConnector
import dbhelper.database.JdbcPostgresConnection
import dbhelper.dsl.conditions.And
import dbhelper.dsl.conditions.And.Companion.AND
import dbhelper.dsl.conditions.Eq
import dbhelper.dsl.conditions.Eq.Companion.EQ
import dbhelper.dsl.conditions.Gt.Companion.GT
import dbhelper.dsl.conditions.Or
import dbhelper.dsl.conditions.Or.Companion.OR
import dbhelper.dsl.fields.As.Companion.AS
import dbhelper.dsl.fields.Column.Companion.col
import dbhelper.dsl.fields.functions.Coalesce.Companion.COALESCE
import dbhelper.dsl.fields.functions.Count.Companion.COUNT
import dbhelper.dsl.query.Query
import dbhelper.dsl.query.Row
import dbhelper.dsl.query.RowParser
import dbhelper.dsl.query.join.On
import dbhelper.dsl.query.join.On.Companion.EQ
import dbhelper.dsl.query.join.On.Companion.ON
import dbhelper.dsl.query.order.Asc
import dbhelper.dsl.query.order.Asc.Companion.ASC
import dbhelper.dsl.query.order.Desc.Companion.DESC
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
class SelectTest {

    companion object {
        @Container
        val postgres = ContainerPostgres()
    }

    private val connectionConfig = JdbcPostgresConnection("localhost:5432/tests", "user", "password")
    private val db = Database.connect(HikariConnector(connectionConfig))

    @Test
    fun `select with where`() {
        val user = db.select(Query()
            .from("users")
            .where(("city" EQ "Milano") AND ("age" GT 18))
            .build()
        ).first { User(str("email"), str("name"), str("city"), int("age")) }

        assertThat(user).isEqualTo(User("vittorio@gialli.it", "Vittorio Gialli", "Milano", 64))
    }

    @Test
    fun `select first not found`() {
        val user = db.select(Query()
            .from("users")
            .where("city" EQ "New York")
            .build()
        ).first { User(str("email"), str("name"), str("city"), int("age")) }

        assertThat(user).isNull()
    }

    @Test
    fun `select all`() {
        val users: List<User> = db.select(Query()
            .from("users")
            .where("city" EQ "Firenze")
            .limit(2)
            .build()
        ).all { User(str("email"), str("name"), str("city"), int("age")) }

        assertThat(users).isEqualTo(
            listOf(
                User(email = "mario@rossi.it", fullName = "Mario Rossi", city = "Firenze", age = 35),
                User(email = "paolo@bianchi.it", fullName = "Paolo Bianchi", city = "Firenze", age = 6)
            )
        )
    }

    @Test
    fun `select only one field`() {
        val userEmail = db.select(Query()
            .from("users")
            .fields("email")
            .where("city" EQ "Lucca")
            .build()
        ).first { str("email") }

        assertThat(userEmail).isEqualTo("luigi@verdi.it")
    }

    @Test
    fun `select multiple cities`() {
        val users = db.select(Query()
            .from("users")
            .where(("city" EQ "Firenze") OR ("city" EQ "Lucca"))
            .limit(3)
            .build()
        ).all { User(str("email"), str("name"), str("city"), int("age")) }

        assertThat(users).isEqualTo(
            listOf(
                User(email = "mario@rossi.it", fullName = "Mario Rossi", city = "Firenze", age = 35),
                User(email = "luigi@verdi.it", fullName = "Luigi Verdi", city = "Lucca", age = 28),
                User(email = "paolo@bianchi.it", fullName = "Paolo Bianchi", city = "Firenze", age = 6)
            )
        )
    }

    @Test
    fun join() {
        val all: List<UserPetsCount> = db.select(Query()
            .fields("users.name", "count(pets.name) as count")
            .from("users")
            .where((("email" EQ "mario@rossi.it") AND ("city" EQ "Firenze")) OR ("users.age" EQ 28))
            .join("pets" ON "pets.owner" EQ "users.email")
            .groupBy("email")
            .build()
        ).all { UserPetsCount(str("name"), int("count")) }

        assertThat(all).isEqualTo(
            listOf(
                UserPetsCount(fullName = "Luigi Verdi", pets = 2)
            )
        )
    }

    @Test
    fun allJavaSyntax() {
        val all: List<UserPetsCount> = db.select(
            Query()
                .fields("users.name", "count(pets.name) as count")
                .from("users")
                .where(
                    Or(
                        And(Eq("email", "mario@rossi.it"), Eq("city", "Firenze")),
                        Eq("users.age", 28)
                    )
                )
                .join(On("pets", "pets.owner", "users.email"))
                .groupBy("email")
                .orderBy(ASC("name"))
                .build()
        ).all(UserPetsCountRowParser())

        assertThat(all).isEqualTo(
            listOf(
                UserPetsCount(fullName = "Luigi Verdi", pets = 2)
            )
        )
    }

    @Test
    fun firstJavaSyntax() {
        val user = db.select(
            Query()
                .fields("users.name", "count(pets.name) as count")
                .from("users")
                .where(
                    Or(
                        And(Eq("email", "mario@rossi.it"), Eq("city", "Firenze")),
                        Eq("users.age", 28)
                    )
                )
                .join(On("pets", "pets.owner", "users.email"))
                .groupBy("email")
                .orderBy(Asc("name"))
                .build()
        ).first(UserPetsCountRowParser())

        assertThat(user).isEqualTo(UserPetsCount(fullName = "Luigi Verdi", pets = 2))
    }

    @Test
    fun firstJavaHybridSyntax() {
        val user: UserPetsCount? = db.select(
            Query()
                .fields("users.name", "count(pets.name) as count")
                .from("users")
                .where((("email" EQ "mario@rossi.it") AND ("city" EQ "Firenze")) OR ("users.age" EQ 28))
                .join("pets" ON "pets.owner" EQ "users.email")
                .groupBy("email")
                .orderBy(Asc("name"))
                .build()
        ).first(UserPetsCountRowParser())

        assertThat(user).isEqualTo(UserPetsCount(fullName = "Luigi Verdi", pets = 2))
    }

    @Test
    fun leftJoin() {
        val all = db.select(Query()
            .fields("users.name".col, COUNT("pets.name") AS "count")
            .from("users")
            .where((("email" EQ "mario@rossi.it") AND ("city" EQ "Firenze")) OR ("users.age" EQ 28))
            .leftJoin("pets" ON "pets.owner" EQ "users.email")
            .groupBy("email")
            .orderBy(DESC("name"))
            .build()
        ).all { UserPetsCount(str("name"), int("count")) }

        assertThat(all).isEqualTo(
            listOf(
                UserPetsCount(fullName = "Mario Rossi", pets = 0),
                UserPetsCount(fullName = "Luigi Verdi", pets = 2)
            )
        )
    }

    @Test
    fun `query raw`() {
        val all = db.select(Query.raw("""SELECT * FROM users WHERE city = 'Firenze';"""))
            .all { User(str("email"), str("name"), str("city"), int("age")) }

        assertThat(all).isEqualTo(
            listOf(
                User(email = "mario@rossi.it", fullName = "Mario Rossi", city = "Firenze", age = 35),
                User(email = "paolo@bianchi.it", fullName = "Paolo Bianchi", city = "Firenze", age = 6),
                User(email = "matteo@renzi.it", fullName = "Matteo Renzi", city = "Firenze", age = 45)
            )
        )
    }

    @Test
    fun `select a delete statement with returning`() {
        val all = db.select(Query.raw("""DELETE FROM pets_deletable RETURNING *;"""))
            .all { Pet(str("name"), str("type"), int("age")) }

        assertThat(all).isEqualTo(listOf(Pet(name="Pluto", type="Dog", age=2), Pet(name="Fido", type="Dog", age=3)))
    }

    @Test
    fun `select coalesce`() {
        val coalesceMail = db.select(Query()
            .fields(COALESCE("city", "none") AS "cit")
            .from("users")
            .where("email" EQ "null@city.it")
            .build()
        ).first { str("cit") }

        assertThat(coalesceMail).isEqualTo("none")
    }
}

data class Pet(val name: String, val type: String, val age: Int)
data class User(val email: String, val fullName: String, val city: String, val age: Int)
data class UserPetsCount(val fullName: String, val pets: Int)

class UserPetsCountRowParser : RowParser<UserPetsCount> {
    override fun parse(row: Row): UserPetsCount {
        return UserPetsCount(row.str("name"), row.int("count"))
    }
}