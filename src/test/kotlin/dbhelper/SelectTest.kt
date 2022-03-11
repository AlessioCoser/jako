package dbhelper

import dbhelper.dsl.*
import dbhelper.dsl.Order.Companion.asc
import dbhelper.dsl.Order.Companion.desc
import dbhelper.dsl.conditions.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.sql.DriverManager.getConnection
import java.sql.ResultSet

@Testcontainers
class SelectTest {

    companion object {
        @Container
        val postgres = ContainerPostgres()
    }

    @Test
    fun `select with where`() {
        val db = Database.connect()
        val user: User = db.select {
            from("users")
            where(("city" eq "Milano") and ("age" gt 18))

        }.first { User(getString("email"), getString("name"), getString("city"), getInt("age")) }

        assertThat(user).isEqualTo(User("vittorio@gialli.it", "Vittorio Gialli", "Milano", 64))
    }

    @Test
    fun `select all`() {
        val db = Database.connect()
        val users: List<User> = db.select {
            from("users")
            where("city" eq "Firenze")
            limit(2)
        }.all { User(getString("email"), getString("name"), getString("city"), getInt("age")) }

        assertThat(users).isEqualTo(
            listOf(
                User(email = "mario@rossi.it", fullName = "Mario Rossi", city = "Firenze", age = 35),
                User(email = "paolo@bianchi.it", fullName = "Paolo Bianchi", city = "Firenze", age = 6)
            )
        )
    }

    @Test
    fun `select only one field`() {
        val db = Database.connect()
        val userEmail = db.select {
            from("users")
            fields("email")
            where("city" eq "Lucca")
        }.first { getString("email") }

        assertThat(userEmail).isEqualTo("luigi@verdi.it")
    }

    @Test
    fun `select multiple cities`() {
        val db = Database.connect()
        val users = db.select {
            from("users")
            where(("city" eq "Firenze") or ("city" eq "Lucca"))
            limit(3)
        }.all { User(getString("email"), getString("name"), getString("city"), getInt("age")) }

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
        val db = Database.connect()
        val all: List<UserPetsCount> = db.select {
            fields("users.name", "count(pets.name) as count")
            from("users")
            where((("email" eq "mario@rossi.it") and ("city" eq "Firenze")) or ("users.age" eq 28))
            join("pets" on ("pets.owner" eq "users.email"))
            groupBy("email")
        }.all { UserPetsCount(getString("name"), getInt("count")) }

        assertThat(all).isEqualTo(listOf(
            UserPetsCount(fullName="Luigi Verdi", pets=2)
        ))
    }

    @Test
    fun allJavaSyntax() {
        val db = Database.connect()

        val all: List<UserPetsCount> = db.select(Query.builder()
            .fields("users.name", "count(pets.name) as count")
            .from("users")
            .where(
                Or(
                    And(Eq("email", "mario@rossi.it"), Eq("city", "Firenze")),
                    Eq("users.age", 28)
                )
            )
            .join(Join("pets", Eq("pets.owner", "users.email")))
            .groupBy("email")
            .orderBy(asc("name"))
        ).all(UserPetsCountParser())

        assertThat(all).isEqualTo(listOf(
            UserPetsCount(fullName="Luigi Verdi", pets=2)
        ))
    }

    @Test
    fun firstJavaSyntax() {
        val db = Database.connect()

        val user: UserPetsCount = db.select(Query.builder()
            .fields("users.name", "count(pets.name) as count")
            .from("users")
            .where(
                Or(
                    And(Eq("email", "mario@rossi.it"), Eq("city", "Firenze")),
                    Eq("users.age", 28)
                )
            )
            .join(Join("pets", Eq("pets.owner", "users.email")))
            .groupBy("email")
            .orderBy(Asc("name"))
        ).first(UserPetsCountParser())

        assertThat(user).isEqualTo(UserPetsCount(fullName="Luigi Verdi", pets=2))
    }

    @Test
    fun leftJoin() {
        val db = Database.connect()
        val all: List<UserPetsCount> = db.select {
//            where2 = (("email" eq "mario@rossi.it") and ("city" eq "Firenze")) or ("users.age" eq 28)
//            fields2 = listOf("users.name", "count(pets.name) as count")
//            from2 = "users"

            fields("users.name", "count(pets.name) as count")
            from("users")
            where((("email" eq "mario@rossi.it") and ("city" eq "Firenze")) or ("users.age" eq 28))
            leftJoin("pets" on ("pets.owner" eq "users.email"))
            groupBy("email")
            orderBy(desc("name"))
        }.all { UserPetsCount(getString("name"), getInt("count")) }

        assertThat(all).isEqualTo(listOf(
            UserPetsCount(fullName="Mario Rossi", pets=0),
            UserPetsCount(fullName="Luigi Verdi", pets=2)
        ))
    }

    @Test
    fun `query raw`() {
        val db = Database.connect()
        val all: List<User> = db.select {
            raw("""SELECT * FROM users WHERE city = 'Firenze';""")
        }.all { User(getString("email"), getString("name"), getString("city"), getInt("age")) }

        assertThat(all).isEqualTo(listOf(
            User(email="mario@rossi.it", fullName="Mario Rossi", city="Firenze", age=35),
            User(email="paolo@bianchi.it", fullName="Paolo Bianchi", city="Firenze", age=6),
            User(email="matteo@renzi.it", fullName="Matteo Renzi", city="Firenze", age=45)
        ))
    }

    private fun connect() = getConnection("jdbc:postgresql://localhost:5432/tests", "user", "password")
}

data class User(val email: String, val fullName: String, val city: String, val age: Int)
data class UserPetsCount(val fullName: String, val pets: Int)

class UserPetsCountParser: QueryRowParser<UserPetsCount> {
    override fun parse(resultSet: ResultSet): UserPetsCount {
        return UserPetsCount(resultSet.getString("name"), resultSet.getInt("count"))
    }
}