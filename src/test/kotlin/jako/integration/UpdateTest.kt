package jako.integration

import jako.database.Database
import jako.database.JdbcConnectionString
import jako.database.SimpleConnector
import jako.dsl.Dialect
import jako.dsl.Dialect.All.PSQL
import jako.dsl.conditions.EQ
import jako.dsl.query.Query
import jako.dsl.update.Update
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

    private val connectionConfig = JdbcConnectionString.postgresql("localhost:5432/tests", "user", "password")
    private val db = Database.connect(SimpleConnector(connectionConfig), PSQL)

    @Test
    fun `update user age`() {
        db.execute(Update()
            .table("users")
            .set("age", 3)
            .where("email" EQ "cavallino@cavallini.it")
        )

        val newAge = db.select(Query()
            .from("users")
            .where("email" EQ "cavallino@cavallini.it")
        ).first { int("age") }

        assertThat(newAge).isEqualTo(3)
    }

    @Test
    fun `update using builder dsl`() {
        db.execute(
            Update()
                .table("users")
                .set("age", 4)
                .where("email" EQ "cavallino@cavallini.it")
        )

        val newAge = db.select(Query()
            .from("users")
            .where("email" EQ "cavallino@cavallini.it")
        ).first { int("age") }

        assertThat(newAge).isEqualTo(4)
    }
}

data class UpdateCustomer(val name: String, val age: Int)
