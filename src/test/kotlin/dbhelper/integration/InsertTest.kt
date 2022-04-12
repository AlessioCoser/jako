package dbhelper.integration

import dbhelper.database.Database
import dbhelper.database.JdbcPostgresConnection
import dbhelper.database.SimpleConnector
import dbhelper.insert.InsertColumn.Companion.SET
import dbhelper.query.conditions.Eq.Companion.EQ
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
            values("name" SET "name1", "age" SET 18)
        }.execute()

        val customer = db.select {
            from("customers")
            where("name" EQ "name1")
        }.first { Customer(str("name"), int("age")) }

        assertThat(customer).isEqualTo(Customer("name1", 18))
    }
}

data class Customer(val name: String, val age: Int)
