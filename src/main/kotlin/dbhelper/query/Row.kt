package dbhelper.query

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp

interface Row {
    fun str(fieldName: String): String
    fun strOrNull(fieldName: String): String?
    fun bool(fieldName: String): Boolean
    fun byte(fieldName: String): Byte
    fun short(fieldName: String): Short
    fun int(fieldName: String): Int
    fun long(fieldName: String): Long
    fun float(fieldName: String): Float
    fun double(fieldName: String): Double
    fun bytes(fieldName: String): ByteArray
    fun date(fieldName: String): Date
    fun time(fieldName: String): Time
    fun timestamp(fieldName: String): Timestamp
}