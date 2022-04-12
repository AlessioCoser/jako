package dbhelper.integration

import dbhelper.database.Database
import dbhelper.database.JdbcPostgresConnection
import dbhelper.database.SimpleConnector
import dbhelper.dsl.conditions.Eq.Companion.EQ
import dbhelper.dsl.update.UpdateBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class UpdateTest {

    companion object {
        @Container
        val postgres = ContainerPostgres()
    }

    private val connectionConfig = JdbcPostgresConnection("localhost:5432/tests", "user", "password")
    private val db = Database.connect(SimpleConnector(connectionConfig))

    @Test
    fun `update user age`() {
        db.update {
            from("users")
            set("age", 3)
            where("email" EQ "cavallino@cavallini.it")
        }.execute()

        val newAge = db.select {
            from("users")
            where("email" EQ "cavallino@cavallini.it")
        }.first { int("age") }

        assertThat(newAge).isEqualTo(3)
    }

    @Test
    fun `update using builder dsl`() {
        db.update(
            UpdateBuilder()
                .from("users")
                .set("age", 4)
                .where("email" EQ "cavallino@cavallini.it")
        ).execute()

        val newAge = db.select {
            from("users")
            where("email" EQ "cavallino@cavallini.it")
        }.first { int("age") }

        assertThat(newAge).isEqualTo(4)
    }
}

data class UpdateCustomer(val name: String, val age: Int)
