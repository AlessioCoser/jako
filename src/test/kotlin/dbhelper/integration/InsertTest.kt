package dbhelper.integration

import dbhelper.database.Database
import dbhelper.database.JdbcPostgresConnection
import dbhelper.database.SimpleConnector
import dbhelper.dsl.conditions.Eq.Companion.EQ
import dbhelper.dsl.insert.InsertBuilder
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

    private val connectionConfig = JdbcPostgresConnection("localhost:5432/tests", "user", "password")
    private val db = Database.connect(SimpleConnector(connectionConfig))

    @Test
    fun `insert city and age`() {
        db.insert {
            into("customers")
            set("name", "name1")
            set("age", 18)
        }.execute()

        val customer = db.select {
            from("customers")
            where("name" EQ "name1")
        }.first { Customer(str("name"), int("age")) }

        assertThat(customer).isEqualTo(Customer("name1", 18))
    }

    @Test
    fun `insert another city and age with build dsl`() {
        db.insert(InsertBuilder()
            .into("customers")
            .set("name", "name2")
            .set("age", 99)
        ).execute()

        val customer = db.select {
            from("customers")
            where("name" EQ "name2")
        }.first { Customer(str("name"), int("age")) }

        assertThat(customer).isEqualTo(Customer("name2", 99))
    }
}

data class Customer(val name: String, val age: Int)