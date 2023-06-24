package jako.database

import jako.dsl.Dialect
import jako.dsl.Statement
import java.io.PrintStream

class StatementPrinter(private val printStream: PrintStream) {
    var enabled: Boolean = false

    fun println(dialect: Dialect, statement: Statement) {
        if (enabled) {
            printStream.println(statement.toSQL(dialect))
        }
    }
}
