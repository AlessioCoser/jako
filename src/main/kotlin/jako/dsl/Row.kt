package jako.dsl

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.util.*

interface Row {
    fun strOrNull(fieldName: String): String?
    fun str(fieldName: String): String
    fun boolOrNull(fieldName: String): Boolean?
    fun bool(fieldName: String): Boolean
    fun shortOrNull(fieldName: String): Short?
    fun short(fieldName: String): Short
    fun intOrNull(fieldName: String): Int?
    fun int(fieldName: String): Int
    fun longOrNull(fieldName: String): Long?
    fun long(fieldName: String): Long
    fun floatOrNull(fieldName: String): Float?
    fun float(fieldName: String): Float
    fun doubleOrNull(fieldName: String): Double?
    fun double(fieldName: String): Double
    fun dateOrNull(fieldName: String): Date?
    fun date(fieldName: String): Date
    fun localDateOrNull(fieldName: String): LocalDate?
    fun localDate(fieldName: String): LocalDate
    fun timeOrNull(fieldName: String): Time?
    fun time(fieldName: String): Time
    fun timestampOrNull(fieldName: String, calendar: Calendar? = null): Timestamp?
    fun timestamp(fieldName: String, calendar: Calendar? = null): Timestamp
    fun bytesOrNull(fieldName: String): ByteArray?
    fun bytes(fieldName: String): ByteArray
}