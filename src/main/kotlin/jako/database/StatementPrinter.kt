package jako.database

import jako.dsl.Statement
import java.io.PrintStream

class StatementPrinter(private val printStream: PrintStream) {
    var enabled: Boolean = false

    fun println(statement: Statement) {
        if (enabled) {
            printStream.println(statement)
        }
    }
}
