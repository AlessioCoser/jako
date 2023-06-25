package jako.integration

import jako.database.Database
import jako.database.JdbcConnectionString.mysql
import jako.database.JdbcConnectionString.postgresql
import jako.dsl.Dialect.All.MYSQL
import jako.dsl.Dialect.All.PSQL
import jako.dsl.query.Query
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

import java.sql.Time
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Testcontainers
class RowSqlTest {

    companion object {
        @Container
        val postgres = ContainerPostgres()
        @Container
        val mysqlDb = ContainerMysql()
    }

    private val psql = Database.connect(postgresql("localhost:5432/tests", "user", "password"), PSQL)
    private val mysql = Database.connect(mysql("localhost:3306/tests", "root", "password"), MYSQL)

    private val ISODateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")

    @Test
    fun `select types non null`() {
        val types: Types? = psql.select(Query().from("types")).first {
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
                Date.from(Instant.parse("1980-01-01T00:00:00+01:00")), LocalDate.of(1980, 1, 1), Time.valueOf(LocalTime.of(1, 2, 3, 123))
            )
        )
    }

    @Test
    fun `select types`() {
        val types: List<Types> = psql.select(Query().from("types")).all {
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

        assertThat(types[0].time).isEqualTo(Time.valueOf(LocalTime.of(1, 2, 3, 123)))
        assertThat(types).isEqualTo(
            listOf(
                Types(
                    1, "str", true, 1, 999, 3, 3.4f, 5.6,
                    Date.from(Instant.parse("1980-01-01T00:00:00+01:00")), LocalDate.of(1980, 1, 1), Time.valueOf(LocalTime.of(1, 2, 3, 123))
                ),
                Types(2, null, null, null, null, null, null, null, null, null, null)
            )
        )
    }

    @Test
    fun `mysql select types non null`() {
        val types: Types = mysql.select(Query().from("types")).first {
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

        assertThat(types.id).isEqualTo(1)
        assertThat(types.string).isEqualTo("str")
        assertThat(types.boolean).isEqualTo(true)
        assertThat(types.short).isEqualTo(1)
        assertThat(types.int).isEqualTo(999)
        assertThat(types.long).isEqualTo(3)
        assertThat(types.float).isEqualTo(3.4f)
        assertThat(types.double).isEqualTo(5.6)
        assertThat(types.date).isEqualTo(ISODateFormat.parse("1980-01-01T01:00Z"))
        assertThat(types.localDate).isEqualTo(LocalDate.of(1980, 1, 1))
        assertThat(types.time).isEqualTo(Time.valueOf(LocalTime.of(2, 2, 3, 123)))
    }

    @Test
    fun `mysql select types`() {
        val types: List<Types> = mysql.select(Query().from("types")).all {
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
                    ISODateFormat.parse("1980-01-01T01:00Z"), LocalDate.of(1980, 1, 1), Time.valueOf(LocalTime.of(2, 2, 3, 123))
                ),
                Types(2, null, null, null, null, null, null, null, null, null, null)
            )
        )
    }

    @Test
    fun `throws exception when str is null`() {
        val errorMessage = assertThrows(IllegalStateException::class.java) {
            psql.select(Query().from("types")).all { str("string") }
        }.message

        assertThat(errorMessage).isEqualTo("Row.str(\"string\") must not be null")
    }

    @Test
    fun `mysql throws exception when str is null`() {
        val errorMessage = assertThrows(IllegalStateException::class.java) {
            mysql.select(Query().from("types")).all { str("string") }
        }.message

        assertThat(errorMessage).isEqualTo("Row.str(\"string\") must not be null")
    }

    @Test
    fun `throws exception when boolean is null`() {
        val errorMessage = assertThrows(IllegalStateException::class.java) {
            psql.select(Query().from("types")).all { bool("boolean") }
        }.message

        assertThat(errorMessage).isEqualTo("Row.bool(\"boolean\") must not be null")
    }

    @Test
    fun `throws exception when short is null`() {
        val errorMessage = assertThrows(IllegalStateException::class.java) {
            psql.select(Query().from("types")).all { short("short") }
        }.message

        assertThat(errorMessage).isEqualTo("Row.short(\"short\") must not be null")
    }

    @Test
    fun `throws exception when int is null`() {
        val errorMessage = assertThrows(IllegalStateException::class.java) {
            psql.select(Query().from("types")).all { int("int") }
        }.message

        assertThat(errorMessage).isEqualTo("Row.int(\"int\") must not be null")
    }

    @Test
    fun `throws exception when long is null`() {
        val errorMessage = assertThrows(IllegalStateException::class.java) {
            psql.select(Query().from("types")).all { long("long") }
        }.message

        assertThat(errorMessage).isEqualTo("Row.long(\"long\") must not be null")
    }

    @Test
    fun `throws exception when float is null`() {
        val errorMessage = assertThrows(IllegalStateException::class.java) {
            psql.select(Query().from("types")).all { float("float") }
        }.message

        assertThat(errorMessage).isEqualTo("Row.float(\"float\") must not be null")
    }

    @Test
    fun `throws exception when double is null`() {
        val errorMessage = assertThrows(IllegalStateException::class.java) {
            psql.select(Query().from("types")).all { double("double") }
        }.message

        assertThat(errorMessage).isEqualTo("Row.double(\"double\") must not be null")
    }

    @Test
    fun `throws exception when date is null`() {
        val errorMessage = assertThrows(IllegalStateException::class.java) {
            psql.select(Query().from("types")).all { date("date") }
        }.message

        assertThat(errorMessage).isEqualTo("Row.date(\"date\") must not be null")
    }

    @Test
    fun `throws exception when localDate is null`() {
        val errorMessage = assertThrows(IllegalStateException::class.java) {
            psql.select(Query().from("types")).all { localDate("local_date") }
        }.message

        assertThat(errorMessage).isEqualTo("Row.localDate(\"local_date\") must not be null")
    }

    @Test
    fun `throws exception when time is null`() {
        val errorMessage = assertThrows(IllegalStateException::class.java) {
            psql.select(Query().from("types")).all { time("time") }
        }.message

        assertThat(errorMessage).isEqualTo("Row.time(\"time\") must not be null")
    }

    @Test
    fun `select timestamps`() {
        val arr: List<Timestamps?> = psql.select(Query().from("types").fields("timestamp", "timestamp_no_zone"))
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
        val found: Timestamps = psql.select(Query().from("types").fields("timestamp", "timestamp_no_zone"))
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
    fun `mysql select timestamps non null`() {
        val found: Timestamps = mysql.select(Query().from("types").fields("timestamp", "timestamp_no_zone"))
            .first { Timestamps(
                timestamp("timestamp"),
                timestamp("timestamp_no_zone", Calendar.getInstance(TimeZone.getTimeZone("UTC"))),
                timestamp("timestamp_no_zone", Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"))),
                timestamp("timestamp_no_zone")
            ) }!!

        assertEquals(Timestamp.valueOf("2013-03-21 11:11:00.0"), found.zone)
        assertEquals(Timestamp.valueOf("2013-03-21 11:11:00.0"), found.noZoneUtc)
        assertEquals(Timestamp.valueOf("2013-03-21 10:11:00.0"), found.noZoneEurope)
        assertEquals(Timestamp.valueOf("2013-03-21 11:11:00.0"), found.noZone)
    }

    @Test
    fun `throws exception when timestamps are null`() {
        val errorMessage = assertThrows(IllegalStateException::class.java) {
            psql.select(Query().from("types").fields("timestamp", "timestamp_no_zone"))
                .all { timestamp("timestamp") }
        }.message

        assertThat(errorMessage).isEqualTo("Row.timestamp(\"timestamp\") must not be null")
    }

    @Test
    fun `mysql throws exception when timestamps are null`() {
        val errorMessage = assertThrows(IllegalStateException::class.java) {
            mysql.select(Query().from("types").fields("timestamp", "timestamp_no_zone"))
                .all { timestamp("timestamp") }
        }.message

        assertThat(errorMessage).isEqualTo("Row.timestamp(\"timestamp\") must not be null")
    }

    @Test
    fun `select bytearrays`() {
        val arr: List<ByteArray?> = psql.select(Query().from("types").fields("bytes"))
            .all { bytesOrNull("bytes") }

        assertArrayEquals(byteArrayOf(92, 48, 48, 48).toTypedArray(), arr[0]!!.toTypedArray())
        assertNull(arr[1])
    }

    @Test
    fun `select bytearrays non null`() {
        val bytearray: ByteArray = psql.select(Query().from("types").fields("bytes"))
            .first { bytes("bytes") }!!

        assertArrayEquals(byteArrayOf(92, 48, 48, 48).toTypedArray(), bytearray.toTypedArray())
    }

    @Test
    fun `throws exception when bytearray is null`() {
        val errorMessage = assertThrows(IllegalStateException::class.java) {
            psql.select(Query().from("types").fields("bytes"))
                .all { bytes("bytes") }
        }.message

        assertThat(errorMessage).isEqualTo("Row.bytes(\"bytes\") must not be null")
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