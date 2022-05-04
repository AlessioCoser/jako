package jako.integration

import jako.database.Database
import jako.database.JdbcConnection
import jako.dsl.conditions.Eq.Companion.EQ
import jako.dsl.insert.Insert
import jako.dsl.query.Query
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

    private val db = Database.connect(JdbcConnection.postgresql("localhost:5432/tests", "user", "password"))

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