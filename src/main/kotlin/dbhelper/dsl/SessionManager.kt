package dbhelper.dsl

import java.io.InputStream
import java.lang.RuntimeException
import java.sql.*

class SessionManager(private val jdbc: String, private val user: String, private val password: String) {
    fun <T> session(fn: Session.() -> T): T {
        return DriverManager.getConnection(jdbc, user, password).use {
            fn(Session(it))
        }
    }
}

class Session(connection: Connection): Connection by connection {
    fun <T> execute(query: Query, parseRow: Row.() -> T): List<T> {
        println(query)
        val resultSet = prepareStatement(query.statement)
            .setParameters(*query.params.toTypedArray())
            .executeQuery()

        val results = mutableListOf<T>()
        while (resultSet.next()) {
            results.add(parseRow(Row(resultSet)))
        }

        return results.toList()
    }

    private fun PreparedStatement.setParameters(vararg params: Any?): PreparedStatement {
        params.forEachIndexed { index, param -> setObject(index + 1, param) }
        return this
    }
}

class Row(resultSet: ResultSet): ResultSet by resultSet {
    fun str(columnLabel: String): String = getString(columnLabel)
    fun bool(columnLabel: String) = getBoolean(columnLabel)
    fun byte(columnLabel: String) = getByte(columnLabel)
    fun short(columnLabel: String) = getShort(columnLabel)
    fun int(columnLabel: String) = getInt(columnLabel)
    fun long(columnLabel: String) = getLong(columnLabel)
    fun float(columnLabel: String) = getFloat(columnLabel)
    fun double(columnLabel: String) = getDouble(columnLabel)
    fun bytes(columnLabel: String): ByteArray = getBytes(columnLabel)
    fun date(columnLabel: String): Date = getDate(columnLabel)
    fun time(columnLabel: String): Time = getTime(columnLabel)
    fun timestamp(columnLabel: String): Timestamp = getTimestamp(columnLabel)
    fun asciiStream(columnLabel: String): InputStream = getAsciiStream(columnLabel)
    fun binaryStream(columnLabel: String): InputStream = getBinaryStream(columnLabel)

    override fun next(): Boolean {
        throw RuntimeException("next method not callable from Row class")
    }
}