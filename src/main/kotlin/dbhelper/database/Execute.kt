package dbhelper.database

import dbhelper.dsl.Statement

class Execute internal constructor(private val transactionManager: TransactionManager, private val statement: Statement) {
    fun execute() {
        transactionManager.useConnection {
            it.prepareStatement(statement).execute()
        }
    }
}
