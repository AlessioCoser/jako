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
                    onEmpty = { User("stra@ng.er", "stranger", "nowhere", 0) }
            ).first { User(it.getString("email"), it.getString("full_name"), it.getString("city"), it.getInt("age")) }

            assertThat(user).isEqualTo(User("vittorio@gialli.it", "Vittorio Gialli", "Milano", 64))
        }
    }

    @Test
    fun `select with on empty`() {
        connect().use { connection ->
            val user = connection.select(
                    table = "users",
                    where = Where(And("city = ?", "Palermo")),
                    onEmpty = { User("stra@ng.er", "stranger", "nowhere", 0) }
            )
                .first { User(it.getString("email"), it.getString("full_name"), it.getString("city"), it.getInt("age")) }

            assertThat(user).isEqualTo(User("stra@ng.er", "stranger", "nowhere", 0))
        }
    }

    private fun connect() = getConnection("jdbc:postgresql://localhost:5432/tests", "user", "password")
}

data class User(val email: String, val fullName: String, val city: String, val age: Int)
