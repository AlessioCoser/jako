package jako.integration

import jako.database.Database
import jako.database.JdbcConnectionString.postgresql
import jako.dsl.Dialect
import jako.dsl.Dialect.All.PSQL
import jako.dsl.query.Query
import jako.dsl.update.Update
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.ByteArrayOutputStream
import java.io.PrintStream

@Testcontainers
class StatementPrinterTest {
    companion object {
        @Container
        val postgres = ContainerPostgres()
    }

    private val connectionString = postgresql("localhost:5432/tests", "user", "password")
    private val outputStream = ByteArrayOutputStream()
    private val db = Database.connect(connectionString, PSQL, PrintStream(outputStream))

    @BeforeEach
    fun setUp() {
        outputStream.reset()
    }

    @Test
    fun `print statement on select first`() {
        db.printStatements(true)

        db.select(Query().from("users")).first { str("city") }

        assertThat(outputStream.toString()).isEqualTo("SELECT * FROM \"users\"\n")
    }

    @Test
    fun `do not print statement on select first`() {
        db.printStatements(false)

        db.select(Query().from("users")).first { str("city") }

        assertThat(outputStream.toString()).isEqualTo("")
    }

    @Test
    fun `print statement on select all`() {
        db.printStatements(true)

        db.select(Query().from("users")).all { strOrNull("city") }

        assertThat(outputStream.toString()).isEqualTo("SELECT * FROM \"users\"\n")
    }

    @Test
    fun `do not print statement on select all`() {
        db.printStatements(false)

        db.select(Query().from("users")).all { strOrNull("city") }

        assertThat(outputStream.toString()).isEqualTo("")
    }

    @Test
    fun `print statement on execute`() {
        db.printStatements(true)

        db.execute(Update().table("users").set("age", 3))

        assertThat(outputStream.toString()).isEqualTo("UPDATE \"users\" SET \"age\" = ?\n")
    }

    @Test
    fun `do not print statement on execute`() {
        db.printStatements(false)

        db.execute(Update().table("users").set("age", 3))

        assertThat(outputStream.toString()).isEqualTo("")
    }
}