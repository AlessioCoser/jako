package jako.database

import jako.dsl.Dialect
import jako.dsl.Statement

class Execute internal constructor(
    private val transactionManager: TransactionManager,
    private val printer: StatementPrinter,
    private val statement: Statement
) {
    fun execute(dialect: Dialect) {
        printer.println(dialect, statement)
        transactionManager.useConnection {
            it.prepareStatement(statement, dialect).executeUpdate()
        }
    }
}
