package jako.database

import jako.dsl.RawStatement
import jako.dsl.Dialect
import jako.dsl.Statement
import java.io.PrintStream


class Database(private val connector: DatabaseConnector, private val dialect: Dialect, printStream: PrintStream = System.out) {
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
        return Execute(transactionManager, statementPrinter, statement).execute(dialect)
    }

    fun execute(statement: String, params: List<Any?> = emptyList()) {
        return Execute(transactionManager, statementPrinter, RawStatement(statement, params)).execute(dialect)
    }

    fun select(statement: Statement): Select {
        return Select(transactionManager, statementPrinter, statement, dialect)
    }

    fun select(statement: String, params: List<Any?> = emptyList()): Select {
        return Select(transactionManager, statementPrinter, RawStatement(statement, params), dialect)
    }

    companion object {
        @JvmStatic
        fun connect(jdbcConnectionString: String, dialect: Dialect, printStream: PrintStream = System.out): Database {
            return connect(SimpleConnector(jdbcConnectionString), dialect, printStream)
        }

        @JvmStatic
        fun connect(connector: DatabaseConnector, dialect: Dialect, printStream: PrintStream = System.out): Database {
            return Database(connector, dialect, printStream)
        }
    }
}
