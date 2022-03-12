package dbhelper.query

import java.io.InputStream
import java.sql.Date
import java.sql.ResultSet
import java.sql.Time
import java.sql.Timestamp

class RowSql(resultSet: ResultSet): Row, ResultSet by resultSet {
    override fun str(fieldName: String): String = getString(fieldName)
    override fun bool(fieldName: String) = getBoolean(fieldName)
    override fun byte(fieldName: String) = getByte(fieldName)
    override fun short(fieldName: String) = getShort(fieldName)
    override fun int(fieldName: String) = getInt(fieldName)
    override fun long(fieldName: String) = getLong(fieldName)
    override fun float(fieldName: String) = getFloat(fieldName)
    override fun double(fieldName: String) = getDouble(fieldName)
    override fun bytes(fieldName: String): ByteArray = getBytes(fieldName)
    override fun date(fieldName: String): Date = getDate(fieldName)
    override fun time(fieldName: String): Time = getTime(fieldName)
    override fun timestamp(fieldName: String): Timestamp = getTimestamp(fieldName)
    override fun asciiStream(fieldName: String): InputStream = getAsciiStream(fieldName)
    override fun binaryStream(fieldName: String): InputStream = getBinaryStream(fieldName)

    override fun next(): Boolean {
        throw RuntimeException("next method not callable from Row class")
    }
}