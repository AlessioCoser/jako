package jako.integration

import jako.database.Database
import jako.database.JdbcConnectionString
import jako.dsl.query.Query
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Testcontainers
class RowSqlTest {

    companion object {
        @Container
        val postgres = ContainerPostgres()
    }

    private val db = Database.connect(JdbcConnectionString.postgresql("localhost:5432/tests", "user", "password"))

    @Test
    fun `select types non null`() {
        val types: Types? = db.select(Query().from("types")).first {
            Types(
                int("id"),
                str("string"),
                bool("boolean"),
                short("short"),
                int("int"),
                long("long"),
                float("float"),
                double("double"),
                date("date"),
                localDate("local_date"),
                time("time")
            )
        }!!

        assertThat(types).isEqualTo(
            Types(
                1, "str", true, 1, 999, 3, 3.4f, 5.6,
                Date.valueOf("1980-01-01"), LocalDate.of(1980, 1, 1), Time.valueOf(LocalTime.of(1, 2, 3, 123))
            )
        )
    }

    @Test
    fun `select types`() {
        val types: List<Types> = db.select(Query().from("types")).all {
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
            )
        }

        assertThat(types).isEqualTo(
            listOf(
                Types(
                    1, "str", true, 1, 999, 3, 3.4f, 5.6,
                    Date.valueOf("1980-01-01"), LocalDate.of(1980, 1, 1), Time.valueOf(LocalTime.of(1, 2, 3, 123))
                ),
                Types(2, null, null, null, null, null, null, null, null, null, null)
            )
        )
    }

    @Test
    fun `select timestamps`() {
        val arr: List<Timestamps?> = db.select(Query().from("types").fields("timestamp", "timestamp_no_zone"))
            .all { Timestamps(
                timestampOrNull("timestamp"),
                timestampOrNull("timestamp_no_zone", Calendar.getInstance(TimeZone.getTimeZone("UTC"))),
                timestampOrNull("timestamp_no_zone", Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"))),
                timestampOrNull("timestamp_no_zone")
            ) }

        assertEquals(Timestamp.valueOf("2013-03-21 16:10:59.897666"), arr[0]!!.zone)
        assertEquals(Timestamp.valueOf("2013-03-21 11:10:59.897666"), arr[0]!!.noZoneUtc)
        assertEquals(Timestamp.valueOf("2013-03-21 10:10:59.897666"), arr[0]!!.noZoneEurope)
        assertEquals(Timestamp.valueOf("2013-03-21 10:10:59.897666"), arr[0]!!.noZone)
        assertEquals(Timestamps(null, null, null, null), arr[1])
    }

    @Test
    fun `select timestamps non null`() {
        val found: Timestamps = db.select(Query().from("types").fields("timestamp", "timestamp_no_zone"))
            .first { Timestamps(
                timestamp("timestamp"),
                timestamp("timestamp_no_zone", Calendar.getInstance(TimeZone.getTimeZone("UTC"))),
                timestamp("timestamp_no_zone", Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"))),
                timestamp("timestamp_no_zone")
            ) }!!

        assertEquals(Timestamp.valueOf("2013-03-21 16:10:59.897666"), found.zone)
        assertEquals(Timestamp.valueOf("2013-03-21 11:10:59.897666"), found.noZoneUtc)
        assertEquals(Timestamp.valueOf("2013-03-21 10:10:59.897666"), found.noZoneEurope)
        assertEquals(Timestamp.valueOf("2013-03-21 10:10:59.897666"), found.noZone)
    }

    @Test
    fun `select bytearrays`() {
        val arr: List<ByteArray?> = db.select(Query().from("types").fields("bytes"))
            .all { bytesOrNull("bytes") }

        assertArrayEquals(byteArrayOf(92, 48, 48, 48).toTypedArray(), arr[0]!!.toTypedArray())
        assertNull(arr[1])
    }

    @Test
    fun `select bytearrays non null`() {
        val bytearray: ByteArray = db.select(Query().from("types").fields("bytes"))
            .first { bytes("bytes") }!!

        assertArrayEquals(byteArrayOf(92, 48, 48, 48).toTypedArray(), bytearray.toTypedArray())
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
)

data class Timestamps(
    val zone: Timestamp?,
    val noZoneUtc: Timestamp?,
    val noZoneEurope: Timestamp?,
    val noZone: Timestamp?
)