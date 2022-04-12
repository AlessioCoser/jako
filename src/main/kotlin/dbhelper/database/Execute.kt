package dbhelper.database

import dbhelper.Statement

class Execute internal constructor(private val transactionManager: TransactionManager, private val statement: Statement) {
    fun execute() {
        transactionManager.useConnection {
            it.prepareStatement(statement).execute()
        }
    }

//    fun executeAndCountAffected(update: Update): Int {
//        return selectFirst(update.countAffected()) { it.getInt("count") } ?: 0
//    }
}
