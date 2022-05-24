package jako.database

import jako.dsl.Row
import java.sql.ResultSet
import java.sql.Time
import java.sql.Timestamp
import java.util.*

class RowSql(private val resultSet: ResultSet) : Row {
    override fun strOrNull(fieldName: String) = nullable(resultSet.getString(fieldName))
    override fun str(fieldName: String) = nonNull(strOrNull(fieldName), "str", fieldName)
    override fun boolOrNull(fieldName: String) = nullable(resultSet.getBoolean(fieldName))
    override fun bool(fieldName: String) = nonNull(boolOrNull(fieldName), "bool", fieldName)
    override fun shortOrNull(fieldName: String) = nullable(resultSet.getShort(fieldName))
    override fun short(fieldName: String) = nonNull(shortOrNull(fieldName), "short", fieldName)
    override fun intOrNull(fieldName: String) = nullable(resultSet.getInt(fieldName))
    override fun int(fieldName: String) = nonNull(intOrNull(fieldName), "int", fieldName)
    override fun longOrNull(fieldName: String) = nullable(resultSet.getLong(fieldName))
    override fun long(fieldName: String) = nonNull(longOrNull(fieldName), "long", fieldName)
    override fun floatOrNull(fieldName: String) = nullable(resultSet.getFloat(fieldName))
    override fun float(fieldName: String) = nonNull(floatOrNull(fieldName), "float", fieldName)
    override fun doubleOrNull(fieldName: String) = nullable(resultSet.getDouble(fieldName))
    override fun double(fieldName: String) = nonNull(doubleOrNull(fieldName), "double", fieldName)
    override fun dateOrNull(fieldName: String) = nullable(resultSet.getDate(fieldName))
    override fun date(fieldName: String) = nonNull(dateOrNull(fieldName), "date", fieldName)
    override fun localDateOrNull(fieldName: String) = dateOrNull(fieldName)?.toLocalDate()
    override fun localDate(fieldName: String) = nonNull(localDateOrNull(fieldName), "localDate", fieldName)
    override fun timeOrNull(fieldName: String): Time? = nullable(resultSet.getTime(fieldName))
    override fun time(fieldName: String): Time = nonNull(timeOrNull(fieldName), "time", fieldName)
    override fun timestampOrNull(fieldName: String, calendar: Calendar?): Timestamp? = nullable(resultSet.getTimestamp(fieldName, calendar))
    override fun timestamp(fieldName: String, calendar: Calendar?): Timestamp = nonNull(timestampOrNull(fieldName, calendar), "timestamp", fieldName)
    override fun bytesOrNull(fieldName: String): ByteArray? = nullable(resultSet.getBytes(fieldName))
    override fun bytes(fieldName: String) = nonNull(bytesOrNull(fieldName), "bytes", fieldName)

    private fun <T> nullable(value: T?): T? {
        if (resultSet.wasNull()) {
            return null
        }
        return value
    }

    private fun <T> nonNull(value: T?, method: String, fieldName: String): T {
        return checkNotNull(value) { "Row.$method(\"$fieldName\") must not be null" }
    }
}
