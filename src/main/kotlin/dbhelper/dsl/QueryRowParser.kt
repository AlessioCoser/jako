package dbhelper.dsl

import java.sql.ResultSet

interface QueryRowParser<T> {
    fun parse(resultSet: ResultSet): T
}
