package dbhelper.integration

import dbhelper.Database
import dbhelper.query.QueryBuilder
import dbhelper.query.Row
import dbhelper.query.RowParser
import dbhelper.query.conditions.And
import dbhelper.query.conditions.And.Companion.AND
import dbhelper.query.conditions.Eq
import dbhelper.query.conditions.Eq.Companion.EQ
import dbhelper.query.conditions.Gt.Companion.GT
import dbhelper.query.conditions.Or
import dbhelper.query.conditions.Or.Companion.OR
import dbhelper.query.join.On
import dbhelper.query.join.On.Companion.EQ
import dbhelper.query.join.On.Companion.ON
import dbhelper.query.order.Asc
import dbhelper.query.order.Asc.Companion.ASC
import dbhelper.query.order.Desc.Companion.DESC
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

    private val db = Database("jdbc:postgresql://localhost:5432/tests", "user", "password")

    @Test
    fun `select with where`() {
        val user: User = db.select {
            from("users")
            where(("city" EQ "Milano") AND ("age" GT 18))
        }.first { User(str("email"), str("name"), str("city"), int("age")) }

        assertThat(user).isEqualTo(User("vittorio@gialli.it", "Vittorio Gialli", "Milano", 64))
    }

    @Test
    fun `select all`() {
        val users: List<User> = db.select {
            from("users")
            where("city" EQ "Firenze")
            limit(2)
        }.all { User(str("email"), str("name"), str("city"), int("age")) }

        assertThat(users).isEqualTo(
            listOf(
                User(email = "mario@rossi.it", fullName = "Mario Rossi", city = "Firenze", age = 35),
                User(email = "paolo@bianchi.it", fullName = "Paolo Bianchi", city = "Firenze", age = 6)
            )
        )
    }

    @Test
    fun `select only one field`() {
        val userEmail = db.select {
            from("users")
            fields("email")
            where("city" EQ "Lucca")
        }.first { str("email") }

        assertThat(userEmail).isEqualTo("luigi@verdi.it")
    }

    @Test
    fun `select multiple cities`() {
        val users = db.select {
            from("users")
            where(("city" EQ "Firenze") OR ("city" EQ "Lucca"))
            limit(3)
        }.all { User(str("email"), str("name"), str("city"), int("age")) }

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
        val all: List<UserPetsCount> = db.select {
            fields("users.name", "count(pets.name) as count")
            from("users")
            where((("email" EQ "mario@rossi.it") AND ("city" EQ "Firenze")) OR ("users.age" EQ 28))
            join("pets" ON "pets.owner" EQ "users.email")
            groupBy("email")
        }.all { UserPetsCount(str("name"), int("count")) }

        assertThat(all).isEqualTo(
            listOf(
                UserPetsCount(fullName = "Luigi Verdi", pets = 2)
            )
        )
    }

    @Test
    fun allJavaSyntax() {
        val all: List<UserPetsCount> = db.select(
            QueryBuilder()
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
        ).all(UserPetsCountRowParser())

        assertThat(all).isEqualTo(
            listOf(
                UserPetsCount(fullName = "Luigi Verdi", pets = 2)
            )
        )
    }

    @Test
    fun firstJavaSyntax() {
        val user: UserPetsCount = db.select(
            QueryBuilder()
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
        ).first(UserPetsCountRowParser())

        assertThat(user).isEqualTo(UserPetsCount(fullName = "Luigi Verdi", pets = 2))
    }

    @Test
    fun firstJavaHybridSyntax() {
        val user: UserPetsCount = db.select(
            QueryBuilder()
                .fields("users.name", "count(pets.name) as count")
                .from("users")
                .where((("email" EQ "mario@rossi.it") AND ("city" EQ "Firenze")) OR ("users.age" EQ 28))
                .join("pets" ON "pets.owner" EQ "users.email")
                .groupBy("email")
                .orderBy(Asc("name"))
        ).first(UserPetsCountRowParser())

        assertThat(user).isEqualTo(UserPetsCount(fullName = "Luigi Verdi", pets = 2))
    }

    @Test
    fun leftJoin() {
        val all = db.select {
            fields("users.name", "count(pets.name) as count")
            from("users")
            where((("email" EQ "mario@rossi.it") AND ("city" EQ "Firenze")) OR ("users.age" EQ 28))
            leftJoin("pets" ON "pets.owner" EQ "users.email")
            groupBy("email")
            orderBy(DESC("name"))
        }.all { UserPetsCount(str("name"), int("count")) }

        assertThat(all).isEqualTo(
            listOf(
                UserPetsCount(fullName = "Mario Rossi", pets = 0),
                UserPetsCount(fullName = "Luigi Verdi", pets = 2)
            )
        )
    }

    @Test
    fun `query raw`() {
        val all = db.select {
            raw("""SELECT * FROM users WHERE city = 'Firenze';""")
        }.all { User(str("email"), str("name"), str("city"), int("age")) }

        assertThat(all).isEqualTo(
            listOf(
                User(email = "mario@rossi.it", fullName = "Mario Rossi", city = "Firenze", age = 35),
                User(email = "paolo@bianchi.it", fullName = "Paolo Bianchi", city = "Firenze", age = 6),
                User(email = "matteo@renzi.it", fullName = "Matteo Renzi", city = "Firenze", age = 45)
            )
        )
    }
}

data class User(val email: String, val fullName: String, val city: String, val age: Int)
data class UserPetsCount(val fullName: String, val pets: Int)

class UserPetsCountRowParser : RowParser<UserPetsCount> {
    override fun parse(row: Row): UserPetsCount {
        return UserPetsCount(row.str("name"), row.int("count"))
    }
}