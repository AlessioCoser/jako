package dbhelper.integration

import dbhelper.database.Database
import dbhelper.database.HikariConnector
import dbhelper.database.JdbcPostgresConnection
import dbhelper.dsl.conditions.Eq.Companion.EQ
import dbhelper.dsl.insert.Insert
import dbhelper.dsl.query.Query
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
class TransactionTest {

    companion object {
        @Container
        val postgres = ContainerPostgres()
    }

    private val connectionConfig = JdbcPostgresConnection("localhost:5432/tests", "user", "password")
    private val db = Database.connect(HikariConnector(connectionConfig))

    @Test
    fun `transaction failure`() {
        assertThrows<RuntimeException> {
            db.useTransaction {
                db.execute(Insert()
                    .into("customers")
                    .set("name", "transaction_test")
                    .set("age", 18)
                )

                assertThat(customerIsPresent("transaction_test")).isTrue

                throw RuntimeException()
            }
        }

        assertThat(customerIsPresent("transaction_test")).isFalse
    }

    private fun customerIsPresent(name: String): Boolean {
        val customer = db.select(Query()
            .from("customers")
            .where("name" EQ name)
        ).first { Customer(str("name"), int("age")) }

        return customer != null
    }
}
