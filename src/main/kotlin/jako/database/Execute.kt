package jako.database

import jako.dsl.Statement

class Execute internal constructor(private val transactionManager: TransactionManager, private val statement: Statement) {
    fun execute() {
        transactionManager.useConnection {
            it.prepareStatement(statement).execute()
        }
    }
}
