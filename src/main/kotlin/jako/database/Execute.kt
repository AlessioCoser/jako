package jako.database

import jako.dsl.Statement

class Execute internal constructor(
    private val transactionManager: TransactionManager,
    private val printer: StatementPrinter,
    private val statement: Statement
) {
    fun execute() {
        printer.println(statement)
        transactionManager.useConnection {
            it.prepareStatement(statement).execute()
        }
    }
}
