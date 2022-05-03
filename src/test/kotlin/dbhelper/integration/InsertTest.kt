package dbhelper.integration

import dbhelper.database.Database
import dbhelper.database.JdbcPostgresConnection
import dbhelper.database.SimpleConnector
import dbhelper.dsl.conditions.Eq.Companion.EQ
import dbhelper.dsl.insert.Insert
import dbhelper.dsl.query.Query
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
        db.execute(Insert()
            .into("customers")
            .set("name", "name1")
            .set("age", 18)
            .build()
        )

        val customer = db.select(Query()
            .from("customers")
            .where("name" EQ "name1")
            .build()
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
            .build()
        ).first { str("name") }

        val customer = db.select(Query()
            .from("customers")
            .where("name" EQ "name2")
            .build()
        ).first { Customer(str("name"), int("age")) }

        assertThat(insertedName).isEqualTo("name2")
        assertThat(customer).isEqualTo(Customer("name2", 99))
    }
}

data class Customer(val name: String, val age: Int)
