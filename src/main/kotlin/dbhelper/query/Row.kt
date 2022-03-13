package dbhelper.query

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp

interface Row {
    fun strOrNull(fieldName: String): String?
    fun str(fieldName: String): String
    fun boolOrNull(fieldName: String): Boolean?
    fun bool(fieldName: String): Boolean
    fun shortOrNull(fieldName: String): Short?
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