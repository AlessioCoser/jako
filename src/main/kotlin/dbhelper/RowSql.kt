package dbhelper

import dbhelper.query.Row
import java.sql.ResultSet
import java.sql.Time
import java.sql.Timestamp

class RowSql(private val resultSet: ResultSet) : Row {
    override fun strOrNull(fieldName: String) = nullable(resultSet.getString(fieldName))
    override fun str(fieldName: String) = strOrNull(fieldName)!!
    override fun boolOrNull(fieldName: String) = nullable(resultSet.getBoolean(fieldName))
    override fun bool(fieldName: String) = boolOrNull(fieldName)!!
    override fun shortOrNull(fieldName: String) = nullable(resultSet.getShort(fieldName))
    override fun short(fieldName: String) = shortOrNull(fieldName)!!
    override fun intOrNull(fieldName: String) = nullable(resultSet.getInt(fieldName))
    override fun int(fieldName: String) = intOrNull(fieldName)!!
    override fun longOrNull(fieldName: String) = nullable(resultSet.getLong(fieldName))
    override fun long(fieldName: String) = longOrNull(fieldName)!!
    override fun floatOrNull(fieldName: String) = nullable(resultSet.getFloat(fieldName))
    override fun float(fieldName: String) = floatOrNull(fieldName)!!
    override fun doubleOrNull(fieldName: String) = nullable(resultSet.getDouble(fieldName))
    override fun double(fieldName: String) = doubleOrNull(fieldName)!!
    override fun dateOrNull(fieldName: String) = nullable(resultSet.getDate(fieldName))
    override fun date(fieldName: String) = dateOrNull(fieldName)!!
    override fun localDateOrNull(fieldName: String) = dateOrNull(fieldName)?.toLocalDate()
    override fun localDate(fieldName: String) = localDateOrNull(fieldName)!!
    override fun timeOrNull(fieldName: String): Time? = nullable(resultSet.getTime(fieldName))
    override fun time(fieldName: String): Time = timeOrNull(fieldName)!!
    override fun timestamp(fieldName: String): Timestamp = resultSet.getTimestamp(fieldName)
    override fun bytes(fieldName: String): ByteArray = resultSet.getBytes(fieldName)

    private fun <T> nullable(value: T?): T? {
        println(value)
        if (resultSet.wasNull()) {
            return null
        }
        return value
    }
}
