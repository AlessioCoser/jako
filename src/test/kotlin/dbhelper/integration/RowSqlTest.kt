package dbhelper.integration

import dbhelper.Database
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.sql.Date
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

@Testcontainers
class RowSqlTest {

    companion object {
        @Container
        val postgres = ContainerPostgres()
    }

    private val db = Database("jdbc:postgresql://localhost:5432/tests", "user", "password")

    @Test
    fun `select types`() {
        val types: List<Types> = db.select {
            from("types")
        }.all {
            Types(
                int("id"),
                strOrNull("string"),
                boolOrNull("boolean"),
                shortOrNull("short"),
                intOrNull("int"),
                longOrNull("long"),
                floatOrNull("float"),
                doubleOrNull("double"),
                dateOrNull("date"),
                localDateOrNull("local_date"),
                timeOrNull("time")
//                time("time"),
//                timestamp("timestamp"),
//                timestamp("timestamp")
//                bytes("bytes"),
            )
        }

        assertThat(types).isEqualTo(
            listOf(
                Types(1, "str", true, 1, 999, 3, 3.4f, 5.6,
                    Date.valueOf("1980-01-01"), LocalDate.of(1980, 1, 1), Time.valueOf(LocalTime.of(1, 2, 3, 123))),
                Types(2, null, null, null, null, null, null, null, null, null, null)
            )
        )
    }
}

data class Types(
    val id: Int,
    val string: String?,
    val boolean: Boolean?,
    val short: Short?,
    val int: Int?,
    val long: Long?,
    val float: Float?,
    val double: Double?,
    val date: Date?,
    val localDate: LocalDate?,
    val time: Time?
//    val time_no_zone: Time,
//    val timestamp: Timestamp,
//    val timestamp_no_zone: Timestamp
//    val bytes: ByteArray,
)