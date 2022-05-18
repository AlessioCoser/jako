package jako.database

import jako.dsl.Statement
import java.io.PrintStream


class Database(private val connector: DatabaseConnector, printStream: PrintStream = System.out) {
    val transactionManager = TransactionManager { connector.connection() }
    private val statementPrinter = StatementPrinter(printStream)

    init {
        transactionManager.check()
    }

    fun printStatements(enabled: Boolean) {
        statementPrinter.enabled = enabled
    }

    inline fun <T> useTransaction(func: (Transaction) -> T): T {
        return transactionManager.useTransaction(func)
    }

    fun execute(statement: Statement) {
        return Execute(transactionManager, statementPrinter, statement).execute()
    }

    fun select(statement: Statement): Select {
        return Select(transactionManager, statementPrinter, statement)
    }

    companion object {
        @JvmStatic
        fun connect(jdbcConnectionString: String, printStream: PrintStream = System.out): Database {
            return connect(SimpleConnector(jdbcConnectionString), printStream)
        }

        @JvmStatic
        fun connect(connector: DatabaseConnector, printStream: PrintStream = System.out): Database {
            return Database(connector, printStream)
        }
    }
}
