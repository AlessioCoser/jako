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
    fun name() {
        connect().use { connection ->
            val user = Select<User>(connection, """"users"""")
                .fields(""""email"""", """"full_name"""", """"city"""", """"age"""")
                .where(""""city" = ?""", "Milano")
                .and(""""age" > ?""", 18)
                .first { User(it.getString("email"), it.getString("full_name"), it.getString("city"), it.getInt("age")) }

            assertThat(user).isEqualTo(User("vittorio@gialli.it", "Vittorio Gialli", "Milano", 64))
        }
    }

    private fun connect() = getConnection("jdbc:postgresql://localhost:5432/tests", "user", "password")
}

data class User(val email: String, val fullName: String, val city: String, val age: Int)
