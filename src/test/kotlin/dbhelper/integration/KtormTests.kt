package dbhelper.integration

import dbhelper.Database
import dbhelper.session.HikariSessionManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.ktorm.dsl.*
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
class OtherTest {

    companion object {
        @Container
        val postgres = ContainerPostgres()
    }

    private val db = Database(HikariSessionManager("jdbc:postgresql://localhost:5432/tests", "user", "password"))

    @Test
    fun leftJoin() {
        val query = db.db.from(Users)
            .leftJoin(Pets, on = Users.email eq Pets.owner)
            .select(Users.name, count(Pets.name).aliased("count"))
            .where(Users.email eq "luigi@verdi.it" and ((Users.city eq "Firenze") or (Users.age less 29)))
            .groupBy(Users.email)
            .orderBy(Users.name.asc())

        val result = query
            .map { row ->
                "${row[Users.name]}|${row[count(Pets.name).aliased("count")]}"
            }

        assertThat(result).isEqualTo(listOf("Luigi Verdi|2"))
    }
}

object Users : Table<Nothing>("users") {
    val email = varchar("email").primaryKey()
    val name = varchar("name")
    val city = varchar("city")
    val age = int("age")
}

object Pets : Table<Nothing>("pets") {
    val name = varchar("name")
    val type = varchar("type")
    val owner = varchar("owner")
    val age = int("age")
}
