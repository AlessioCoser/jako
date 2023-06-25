package jako.integration

import jako.database.Database
import jako.database.JdbcConnectionString.postgresql
import jako.database.JdbcConnectionString.mysql
import jako.dsl.RawStatement
import jako.dsl.Row
import jako.dsl.RowParser
import jako.dsl.Dialect.All.MYSQL
import jako.dsl.Dialect.All.PSQL
import jako.dsl.conditions.*
import jako.dsl.delete.Delete
import jako.dsl.fields.ALL
import jako.dsl.fields.AS
import jako.dsl.fields.col
import jako.dsl.fields.functions.COALESCE
import jako.dsl.fields.functions.COUNT
import jako.dsl.query.Query
import jako.dsl.query.join.EQ
import jako.dsl.query.join.ON
import jako.dsl.query.join.On
import jako.dsl.query.order.ASC
import jako.dsl.query.order.Asc
import jako.dsl.query.order.DESC
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
class SelectTest {

    companion object {
        @Container
        val postgres = ContainerPostgres()
        @Container
        val mysqlDb = ContainerMysql()
    }

    private val psql = Database.connect(postgresql("localhost:5432/tests", "user", "password"), PSQL)
    private val mysql = Database.connect(mysql("localhost:3306/tests", "root", "password"), MYSQL)

    @Test
    fun `select using a simple connection`() {
        val db = Database.connect("jdbc:postgresql://localhost:5432/tests?user=user&password=password", PSQL)
        val userNames = db.select(Query().from("users")).all { str("name") }

        assertThat(userNames).isEqualTo(listOf(
            "Mario Rossi", "Luigi Verdi", "Paolo Bianchi", "Matteo Renzi",
            "Marco Verdi", "Vittorio Gialli", "Cavallino Cavallini", "Null City"
        ))
    }

    @Test
    fun `select with where`() {
        val user = psql.select(Query()
            .from("users")
            .where(("city" ENDS_WITH "lano") AND ("age" GT 18))
        ).first { User(str("email"), str("name"), str("city"), int("age")) }

        assertThat(user).isEqualTo(User("vittorio@gialli.it", "Vittorio Gialli", "Milano", 64))
    }

    @Test
    fun `select first not found`() {
        val user = psql.select(Query()
            .from("users")
            .where("city" EQ "New York")
        ).first { User(str("email"), str("name"), str("city"), int("age")) }

        assertThat(user).isNull()
    }

    @Test
    fun `select all`() {
        val users: List<User> = psql.select(Query()
            .from("users")
            .where("city" EQ "Firenze")
            .limit(2)
        ).all { User(str("email"), str("name"), str("city"), int("age")) }

        assertThat(users).isEqualTo(
            listOf(
                User(email = "mario@rossi.it", fullName = "Mario Rossi", city = "Firenze", age = 35),
                User(email = "paolo@bianchi.it", fullName = "Paolo Bianchi", city = "Firenze", age = 6)
            )
        )
    }

    @Test
    fun `select only one field`() {
        val userEmail = psql.select(Query()
            .from("users")
            .fields("email")
            .where("city" EQ "Lucca")
        ).first { str("email") }

        assertThat(userEmail).isEqualTo("luigi@verdi.it")
    }

    @Test
    fun `select multiple cities`() {
        val users = psql.select(Query()
            .from("users")
            .where(("city" EQ "Firenze") OR ("city" EQ "Lucca"))
            .limit(3)
        ).all { User(str("email"), str("name"), str("city"), int("age")) }

        assertThat(users).isEqualTo(
            listOf(
                User(email = "mario@rossi.it", fullName = "Mario Rossi", city = "Firenze", age = 35),
                User(email = "luigi@verdi.it", fullName = "Luigi Verdi", city = "Lucca", age = 28),
                User(email = "paolo@bianchi.it", fullName = "Paolo Bianchi", city = "Firenze", age = 6)
            )
        )
    }

    @Test
    fun join() {
        val all: List<UserPetsCount> = psql.select(Query()
            .fields("users.name", "count(pets.name) as count")
            .from("users")
            .where((("email" EQ "mario@rossi.it") AND ("city" EQ "Firenze")) OR ("users.age" EQ 28))
            .join("pets" ON "pets.owner" EQ "users.email")
            .groupBy("email")
        ).all { UserPetsCount(str("name"), int("count")) }

        assertThat(all).isEqualTo(
            listOf(
                UserPetsCount(fullName = "Luigi Verdi", pets = 2)
            )
        )
    }


    @Test
    fun mysqlJoin() {
        val all: List<UserPetsCount> = mysql.select(Query()
            .fields("users.name", "count(pets.name) as count")
            .from("users")
            .where((("email" EQ "mario@rossi.it") AND ("city" EQ "Firenze")) OR ("users.age" EQ 28))
            .join("pets" ON "pets.owner" EQ "users.email")
            .groupBy("email")
        ).all { UserPetsCount(str("name"), int("count")) }

        assertThat(all).isEqualTo(
            listOf(
                UserPetsCount(fullName = "Luigi Verdi", pets = 2)
            )
        )
    }

    @Test
    fun allJavaSyntax() {
        val all: List<UserPetsCount> = psql.select(
            Query()
                .fields("users.name", "count(pets.name) as count")
                .from("users")
                .where(
                    Or(
                        And(Eq("email", "mario@rossi.it"), Eq("city", "Firenze")),
                        Eq("users.age", 28)
                    )
                )
                .join(On("pets", "pets.owner", "users.email"))
                .groupBy("email")
                .orderBy(ASC("name"))
        ).all(UserPetsCountRowParser())

        assertThat(all).isEqualTo(
            listOf(
                UserPetsCount(fullName = "Luigi Verdi", pets = 2)
            )
        )
    }

    @Test
    fun firstJavaSyntax() {
        val user = psql.select(
            Query()
                .fields("users.name", "count(pets.name) as count")
                .from("users")
                .where(
                    Or(
                        And(Eq("email", "mario@rossi.it"), Eq("city", "Firenze")),
                        Eq("users.age", 28)
                    )
                )
                .join(On("pets", "pets.owner", "users.email"))
                .groupBy("email")
                .orderBy(Asc("name"))
        ).first(UserPetsCountRowParser())

        assertThat(user).isEqualTo(UserPetsCount(fullName = "Luigi Verdi", pets = 2))
    }

    @Test
    fun mysqlFirstJavaSyntax() {
        val user = mysql.select(
            Query()
                .fields("users.name", "count(pets.name) as count")
                .from("users")
                .where(
                    Or(
                        And(Eq("email", "mario@rossi.it"), Eq("city", "Firenze")),
                        Eq("users.age", 28)
                    )
                )
                .join(On("pets", "pets.owner", "users.email"))
                .groupBy("email")
                .orderBy(Asc("name"))
        ).first(UserPetsCountRowParser())

        assertThat(user).isEqualTo(UserPetsCount(fullName = "Luigi Verdi", pets = 2))
    }

    @Test
    fun firstJavaHybridSyntax() {
        val user: UserPetsCount? = psql.select(
            Query()
                .fields("users.name", "count(pets.name) as count")
                .from("users")
                .where((("email" EQ "mario@rossi.it") AND ("city" EQ "Firenze")) OR ("users.age" EQ 28))
                .join("pets" ON "pets.owner" EQ "users.email")
                .groupBy("email")
                .orderBy(Asc("name"))
        ).first(UserPetsCountRowParser())

        assertThat(user).isEqualTo(UserPetsCount(fullName = "Luigi Verdi", pets = 2))
    }

    @Test
    fun leftJoin() {
        val all = psql.select(Query()
            .fields("users.name".col, COUNT("pets.name") AS "count")
            .from("users")
            .where((("email" EQ "mario@rossi.it") AND ("city" EQ "Firenze")) OR ("users.age" EQ 28))
            .leftJoin("pets" ON "pets.owner" EQ "users.email")
            .groupBy("email")
            .orderBy(DESC("name"))
        ).all { UserPetsCount(str("name"), int("count")) }

        assertThat(all).isEqualTo(
            listOf(
                UserPetsCount(fullName = "Mario Rossi", pets = 0),
                UserPetsCount(fullName = "Luigi Verdi", pets = 2)
            )
        )
    }

    @Test
    fun mysqlLeftJoin() {
        val all = mysql.select(Query()
            .fields("users.name".col, COUNT("pets.name") AS "count")
            .from("users")
            .where((("email" EQ "mario@rossi.it") AND ("city" EQ "Firenze")) OR ("users.age" EQ 28))
            .leftJoin("pets" ON "pets.owner" EQ "users.email")
            .groupBy("email")
            .orderBy(DESC("name"))
        ).all { UserPetsCount(str("name"), int("count")) }

        assertThat(all).isEqualTo(
            listOf(
                UserPetsCount(fullName = "Mario Rossi", pets = 0),
                UserPetsCount(fullName = "Luigi Verdi", pets = 2)
            )
        )
    }

    @Test
    fun `query raw`() {
        val all = psql.select(RawStatement("""SELECT * FROM users WHERE city = ?;""", listOf("Firenze")))
            .all { User(str("email"), str("name"), str("city"), int("age")) }

        assertThat(all).isEqualTo(
            listOf(
                User(email = "mario@rossi.it", fullName = "Mario Rossi", city = "Firenze", age = 35),
                User(email = "paolo@bianchi.it", fullName = "Paolo Bianchi", city = "Firenze", age = 6),
                User(email = "matteo@renzi.it", fullName = "Matteo Renzi", city = "Firenze", age = 45)
            )
        )
    }

    @Test
    fun `query raw without the RawStatement instantiation`() {
        val all = psql.select("""SELECT * FROM users WHERE city = 'Firenze';""")
            .all { User(str("email"), str("name"), str("city"), int("age")) }

        assertThat(all).isEqualTo(
            listOf(
                User(email = "mario@rossi.it", fullName = "Mario Rossi", city = "Firenze", age = 35),
                User(email = "paolo@bianchi.it", fullName = "Paolo Bianchi", city = "Firenze", age = 6),
                User(email = "matteo@renzi.it", fullName = "Matteo Renzi", city = "Firenze", age = 45)
            )
        )
    }

    @Test
    fun `select a delete statement with returning`() {
        val all = psql.select(Delete.from("pets_deletable").returning(ALL))
            .all { Pet(str("name"), str("type"), int("age")) }

        assertThat(all).isEqualTo(listOf(Pet(name="Pluto", type="Dog", age=2), Pet(name="Fido", type="Dog", age=3)))
    }

    @Test
    fun `select coalesce`() {
        val coalesceMail = psql.select(Query()
            .fields(COALESCE("city", "none") AS "cit")
            .from("users")
            .where("email" EQ "null@city.it")
        ).first { str("cit") }

        assertThat(coalesceMail).isEqualTo("none")
    }

    @Test
    fun `mysql select coalesce`() {
        val coalesceMail = mysql.select(Query()
            .fields(COALESCE("city", "none") AS "cit")
            .from("users")
            .where("email" EQ "null@city.it")
        ).first { str("cit") }

        assertThat(coalesceMail).isEqualTo("none")
    }
}

data class Pet(val name: String, val type: String, val age: Int)
data class User(val email: String, val fullName: String, val city: String, val age: Int)
data class UserPetsCount(val fullName: String, val pets: Int)

class UserPetsCountRowParser : RowParser<UserPetsCount> {
    override fun parse(row: Row): UserPetsCount {
        return UserPetsCount(row.str("name"), row.int("count"))
    }
}