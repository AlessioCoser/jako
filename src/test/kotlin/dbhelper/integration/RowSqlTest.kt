package dbhelper.integration

import dbhelper.Database
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.shaded.com.google.common.primitives.Bytes
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp

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
                bool("boolean"),
                shortOrNull("short")
//                int("int"),
//                long("long"),
//                float("float"),
//                double("double"),
//                bytes("bytes"),
//                date("date"),
//                time("time"),
//                time("time"),
//                timestamp("timestamp"),
//                timestamp("timestamp")
            )
        }

        assertThat(types).isEqualTo(listOf(
            Types(1, "str", true, 1),
            Types(2, null, false, null)
        ))
    }
}

data class Types(
    val id: Int,
    val string: String?,
    val boolean: Boolean?,
    val short: Short?
//    val int: Int?,
//    val long: Long,
//    val float: Float,
//    val double: Double,
//    val bytes: ByteArray,
//    val date: Date,
//    val time: Time,
//    val time_no_zone: Time,
//    val timestamp: Timestamp,
//    val timestamp_no_zone: Timestamp
)