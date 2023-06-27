package jako.integration

import jako.database.Database
import jako.database.JdbcConnectionString
import jako.dsl.Dialect
import jako.dsl.conditions.EQ
import jako.dsl.insert.Insert
import jako.dsl.query.Query
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
class TransactionPsqlTest {

    companion object {
        @Container
        val databaseInstance = ContainerPostgres()
    }

    private val db = Database.connect(JdbcConnectionString.postgresql("localhost:5432/tests", "user", "password"), Dialect.PSQL)

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
