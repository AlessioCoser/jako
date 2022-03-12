package dbhelper.dsl

import java.io.InputStream
import java.sql.Date
import java.sql.ResultSet
import java.sql.Time
import java.sql.Timestamp

class Row(resultSet: ResultSet): ResultSet by resultSet {
    fun str(fieldName: String): String = getString(fieldName)
    fun bool(fieldName: String) = getBoolean(fieldName)
    fun byte(fieldName: String) = getByte(fieldName)
    fun short(fieldName: String) = getShort(fieldName)
    fun int(fieldName: String) = getInt(fieldName)
    fun long(fieldName: String) = getLong(fieldName)
    fun float(fieldName: String) = getFloat(fieldName)
    fun double(fieldName: String) = getDouble(fieldName)
    fun bytes(fieldName: String): ByteArray = getBytes(fieldName)
    fun date(fieldName: String): Date = getDate(fieldName)
    fun time(fieldName: String): Time = getTime(fieldName)
    fun timestamp(fieldName: String): Timestamp = getTimestamp(fieldName)
    fun asciiStream(fieldName: String): InputStream = getAsciiStream(fieldName)
    fun binaryStream(fieldName: String): InputStream = getBinaryStream(fieldName)

    override fun next(): Boolean {
        throw RuntimeException("next method not callable from Row class")
    }
}