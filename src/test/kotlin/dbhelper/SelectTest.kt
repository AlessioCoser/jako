package dbhelper

import dbhelper.dsl.*
import dbhelper.dsl.conditions.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
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
        connect().use { connection ->
            val user = connection.select(
                table = "users",
                where = ("city" eq "Milano") and ("age" gt 18),
                map = { User(it.getString("email"), it.getString("name"), it.getString("city"), it.getInt("age")) }
            ).first()

            assertThat(user).isEqualTo(User("vittorio@gialli.it", "Vittorio Gialli", "Milano", 64))
        }
    }

    @Test
    fun `select with on empty`() {
        connect().use { connection ->
            val user = connection.select(
                table = "users",
                where = "city" eq "Palermo",
                map = { User(it.getString("email"), it.getString("name"), it.getString("city"), it.getInt("age")) },
                onEmpty = { User("stra@ng.er", "stranger", "nowhere", 0) }
            ).first()

            assertThat(user).isEqualTo(User("stra@ng.er", "stranger", "nowhere", 0))
        }
    }

    @Test
    fun `select all`() {
        connect().use { connection ->
            val users = connection.select(
                table = "users",
                where = "city" eq "Firenze",
                limit = 2,
                map = { User(it.getString("email"), it.getString("name"), it.getString("city"), it.getInt("age")) }
            ).all()

            assertThat(users).isEqualTo(
                listOf(
                    User(email = "mario@rossi.it", fullName = "Mario Rossi", city = "Firenze", age = 35),
                    User(email = "paolo@bianchi.it", fullName = "Paolo Bianchi", city = "Firenze", age = 6)
                )
            )
        }
    }

    @Test
    fun `select only one field`() {
        connect().use { connection ->
            val userEmail = connection.select(
                table = "users",
                fields = listOf("email"),
                where = "city" eq "Lucca",
                map = { it.getString("email") }
            ).first()

            assertThat(userEmail).isEqualTo("luigi@verdi.it")
        }
    }

    @Test
    fun `select multiple cities`() {
        connect().use { connection ->
            val users = connection.select(
                table = "users",
                where = ("city" eq "Firenze") or ("city" eq "Lucca"),
                limit = 3,
                map = { User(it.getString("email"), it.getString("name"), it.getString("city"), it.getInt("age")) }
            ).all()

            assertThat(users).isEqualTo(
                listOf(
                    User(email = "mario@rossi.it", fullName = "Mario Rossi", city = "Firenze", age = 35),
                    User(email = "luigi@verdi.it", fullName = "Luigi Verdi", city = "Lucca", age = 28),
                    User(email = "paolo@bianchi.it", fullName = "Paolo Bianchi", city = "Firenze", age = 6)
                )
            )
        }
    }

    @Test
    @Disabled
    fun `select join`() {
        connect().use { connection ->
            val users = connection.select(
                table = "users",
                where = ("city" eq "Firenze") or ("city" eq "Lucca"),
                joins = listOf(
                    LeftJoin("table2" on ("table2.test" eq "users.email")),
                    InnerJoin("table" on ("table.test" eq "users.id"))
                ),
                limit = 3,
                orderBy = "email",
                map = { User(it.getString("email"), it.getString("name"), it.getString("city"), it.getInt("age")) }
            ).all()

            assertThat(users).isEqualTo(
                listOf(
                    User(email = "mario@rossi.it", fullName = "Mario Rossi", city = "Firenze", age = 35),
                    User(email = "luigi@verdi.it", fullName = "Luigi Verdi", city = "Lucca", age = 28),
                    User(email = "paolo@bianchi.it", fullName = "Paolo Bianchi", city = "Firenze", age = 6)
                )
            )
        }
    }

    @Test
    fun name() {
        val db = Database.connect()
        val user: User = db.select {
            fields("email", "name", "city", "age")
            from("users")
        }.first { User(getString("email"), getString("name"), getString("city"), getInt("age")) }

        assertThat(user).isEqualTo(User(email = "mario@rossi.it", fullName = "Mario Rossi", city = "Firenze", age = 35))
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
            .join(GenericJoin("pets", Eq("pets.owner", "users.email")))
            .groupBy("email")
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
            .join(GenericJoin("pets", Eq("pets.owner", "users.email")))
            .groupBy("email")
        ).first(UserPetsCountParser())

        assertThat(user).isEqualTo(UserPetsCount(fullName="Luigi Verdi", pets=2))
    }

    @Test
    fun leftJoin() {
        val db = Database.connect()
        val all: List<UserPetsCount> = db.select {
            fields("users.name", "count(pets.name) as count")
            from("users")
            where((("email" eq "mario@rossi.it") and ("city" eq "Firenze")) or ("users.age" eq 28))
            leftJoin("pets" on ("pets.owner" eq "users.email"))
            groupBy("email")
        }.all { UserPetsCount(getString("name"), getInt("count")) }

        assertThat(all).isEqualTo(listOf(
            UserPetsCount(fullName="Luigi Verdi", pets=2),
            UserPetsCount(fullName="Mario Rossi", pets=0)
        ))
    }

    @Test
    fun `join params style`() {
        connect().use { connection ->
            val all = connection.select(
                fields = listOf("users.name", "count(pets.name) as count"),
                table = "users",
                where = (("email" eq "mario@rossi.it") and ("city" eq "Firenze")) or ("users.age" eq 28),
                limit = 3,
                joins = listOf(
                    LeftJoin("pets" on ("pets.owner" eq "users.email"))
                ),
                groupBy = "email",
                map = { UserPetsCount(it.getString("name"), it.getInt("count")) }
            ).all()

            assertThat(all).isEqualTo(listOf(
                UserPetsCount(fullName="Luigi Verdi", pets=2),
                UserPetsCount(fullName="Mario Rossi", pets=0)
            ))
        }
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