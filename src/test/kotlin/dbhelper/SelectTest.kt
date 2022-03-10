package dbhelper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.sql.DriverManager.getConnection

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
                where = Where(
                    And("city = ?", "Milano"),
                    And("age > ?", 18)
                ),
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
                where = Where(And("city = ?", "Palermo")),
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
                where = Where(And("city = ?", "Firenze")),
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
                where = Where(And("city = ?", "Lucca")),
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
                where = Where(And("(city = ? OR city = ?)", "Firenze", "Lucca")),
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
    fun `select join`() {
        connect().use { connection ->
            val users = connection.select(
                table = "users",
                where = Where(And("(city = ? OR city = ?)", "Firenze", "Lucca")),
                joins = Joins(
                    LeftJoin("table2 ON table2.test = users.email"),
                    InnerJoin("table ON table.test = users.id")
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

    private fun connect() = getConnection("jdbc:postgresql://localhost:5432/tests", "user", "password")
}

data class User(val email: String, val fullName: String, val city: String, val age: Int)
