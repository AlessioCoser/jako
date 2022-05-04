package jako.integration

import jako.database.Database
import jako.database.JdbcConnectionString
import jako.database.SimpleConnector
import jako.dsl.conditions.Eq.Companion.EQ
import jako.dsl.insert.Insert
import jako.dsl.query.Query
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

    private val connectionConfig = JdbcConnectionString.postgresql("localhost:5432/tests", "user", "password")
    private val db = Database.connect(SimpleConnector(connectionConfig))

    @Test
    fun `insert city and age`() {
        db.execute(Insert()
            .into("customers")
            .set("name", "name1")
            .set("age", 18)
        )

        val customer = db.select(Query()
            .from("customers")
            .where("name" EQ "name1")
        ).first { Customer(str("name"), int("age")) }

        assertThat(customer).isEqualTo(Customer("name1", 18))
    }

    @Test
    fun `return inserted field`() {
        val insertedName = db.select(Insert()
            .into("customers")
            .set("name", "name2")
            .set("age", 99)
            .returning("name")
        ).first { str("name") }

        val customer = db.select(Query()
            .from("customers")
            .where("name" EQ "name2")
        ).first { Customer(str("name"), int("age")) }

        assertThat(insertedName).isEqualTo("name2")
        assertThat(customer).isEqualTo(Customer("name2", 99))
    }
}

data class Customer(val name: String, val age: Int)
