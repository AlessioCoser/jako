package jako.integration

import jako.database.Database
import jako.database.JdbcConnectionString.mysql
import jako.database.JdbcConnectionString.postgresql
import jako.database.SimpleConnector
import jako.dsl.Dialect.All.MYSQL
import jako.dsl.Dialect.All.PSQL
import jako.dsl.conditions.EQ
import jako.dsl.insert.Insert
import jako.dsl.query.Query
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class InsertTest {

    companion object {
        @Container
        val postgresDb = ContainerPostgres()
        @Container
        val mysqlDb = ContainerMysql()
    }

    private val psql = Database.connect(SimpleConnector(postgresql("localhost:5432/tests", "user", "password")), PSQL)

    private val mysql = Database.connect(SimpleConnector(mysql("localhost:3306/tests", "root", "password")), MYSQL)

    @Test
    fun `insert city and age`() {
        psql.execute(Insert()
            .into("customers")
            .set("name", "name1")
            .set("age", 18)
        )

        val customer = psql.select(Query()
            .from("customers")
            .where("name" EQ "name1")
        ).first { Customer(str("name"), int("age")) }

        assertThat(customer).isEqualTo(Customer("name1", 18))
    }

    @Test
    fun `insert city and age with a raw statement`() {
        psql.execute("""INSERT INTO "customers" ("name", "age") VALUES ('name2', 19)""")

        val customer = psql.select(Query()
            .from("customers")
            .where("name" EQ "name2")
        ).first { Customer(str("name"), int("age")) }

        assertThat(customer).isEqualTo(Customer("name2", 19))
    }

    @Test
    fun `insert city and age with a RawStatement`() {
        psql.execute("""INSERT INTO customers(name, age) VALUES (?, ?)""", listOf("name9", 99))

        val customer = psql.select(Query()
            .from("customers")
            .where("name" EQ "name9")
        ).first { Customer(str("name"), int("age")) }

        assertThat(customer).isEqualTo(Customer("name9", 99))
    }

    @Test
    fun `return inserted field`() {
        val insertedName = psql.select(Insert()
            .into("customers")
            .set("name", "name3")
            .set("age", 99)
            .returning("name")
        ).first { str("name") }

        val customer = psql.select(Query()
            .from("customers")
            .where("name" EQ "name3")
        ).first { Customer(str("name"), int("age")) }

        assertThat(insertedName).isEqualTo("name3")
        assertThat(customer).isEqualTo(Customer("name3", 99))
    }


    @Test
    fun `mysql insert city and age`() {
        mysql.execute(Insert()
            .into("customers")
            .set("name", "name1")
            .set("age", 18)
        )

        val customer = mysql.select(Query()
            .from("customers")
            .where("name" EQ "name1")
        ).first { Customer(str("name"), int("age")) }

        assertThat(customer).isEqualTo(Customer("name1", 18))
    }

    @Test
    fun `Cannot use RETURNING statement with MYSQL dialect`() {
        val message = assertThrows<RuntimeException> {
            mysql.execute(Insert()
                .into("customers")
                .set("name", "name2")
                .set("age", 99)
                .returning("name")
            )
        }.message

        assertThat(message).isEqualTo("Cannot use RETURNING statement with MYSQL dialect")
    }
}

data class Customer(val name: String, val age: Int)
