package dbhelper

import dbhelper.query.Row
import java.io.InputStream
import java.sql.Date
import java.sql.ResultSet
import java.sql.Time
import java.sql.Timestamp

class RowSql(private val resultSet: ResultSet) : Row {
    override fun str(fieldName: String): String = resultSet.getString(fieldName)
    override fun bool(fieldName: String) = resultSet.getBoolean(fieldName)
    override fun byte(fieldName: String) = resultSet.getByte(fieldName)
    override fun short(fieldName: String) = resultSet.getShort(fieldName)
    override fun int(fieldName: String) = resultSet.getInt(fieldName)
    override fun long(fieldName: String) = resultSet.getLong(fieldName)
    override fun float(fieldName: String) = resultSet.getFloat(fieldName)
    override fun double(fieldName: String) = resultSet.getDouble(fieldName)
    override fun bytes(fieldName: String): ByteArray = resultSet.getBytes(fieldName)
    override fun date(fieldName: String): Date = resultSet.getDate(fieldName)
    override fun time(fieldName: String): Time = resultSet.getTime(fieldName)
    override fun timestamp(fieldName: String): Timestamp = resultSet.getTimestamp(fieldName)
    override fun asciiStream(fieldName: String): InputStream = resultSet.getAsciiStream(fieldName)
    override fun binaryStream(fieldName: String): InputStream = resultSet.getBinaryStream(fieldName)
}
