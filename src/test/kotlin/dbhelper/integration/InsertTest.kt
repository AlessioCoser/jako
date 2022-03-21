package dbhelper.integration

import dbhelper.Database
import dbhelper.insert.Column.Companion.SET
import dbhelper.query.conditions.Eq.Companion.EQ
import dbhelper.session.HikariSessionManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class InsertTest {

    companion object {
        @Container
        val postgres = ContainerPostgres()
    }

    private val db = Database(HikariSessionManager("jdbc:postgresql://localhost:5432/tests", "user", "password"))

    @Test
    fun `insert city and age`() {
        db.insert {
            into("customers")
            values("name" SET "name1", "age" SET 18)
        }

        val customer = db.select {
            from("customers")
            where("name" EQ "name1")
        }.first { Customer(str("name"), int("age")) }

        assertThat(customer).isEqualTo(Customer("name1", 18))
    }
}

data class Customer(val name: String, val age: Int)
