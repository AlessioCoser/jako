package dbhelper.database

import java.sql.Connection
import java.sql.SQLException

class TransactionManager(private val connector: () -> Connection) {
    private val threadLocal = ThreadLocal<Transaction>()
    val currentTransaction: Transaction? get() = threadLocal.get()

    fun newTransaction(): Transaction {
        if (currentTransaction != null) {
            throw IllegalStateException("Current thread is already in a transaction.")
        }

        return JdbcTransaction().apply { threadLocal.set(this) }
    }

    fun newConnection(): Connection {
        return connector.invoke()
    }

    inline fun <T> useConnection(func: (Connection) -> T): T {
        try {
            val transaction = currentTransaction
            val connection = transaction?.connection ?: newConnection()

            try {
                return func(connection)
            } finally {
                if (transaction == null) connection.close()
            }
        } catch (e: SQLException) {
            throw e
        }
    }

    inline fun <T> useTransaction(func: (Transaction) -> T): T {
        val current = currentTransaction
        val isOuter = current == null
        val transaction = current ?: newTransaction()
        var throwable: Throwable? = null

        try {
            return func(transaction)
        } catch (e: SQLException) {
            throwable = e
            throw throwable
        } catch (e: Throwable) {
            throwable = e
            throw throwable
        } finally {
            if (isOuter) {
                @Suppress("ConvertTryFinallyToUseCall")
                try {
                    if (throwable == null) transaction.commit() else transaction.rollback()
                } finally {
                    transaction.close()
                }
            }
        }
    }

    private inner class JdbcTransaction : Transaction {
        private var originAutoCommit = true

        private val connectionLazy = lazy(LazyThreadSafetyMode.NONE) {
            newConnection().apply {
                try {
                    originAutoCommit = autoCommit
                    if (originAutoCommit) {
                        autoCommit = false
                    }
                } catch (e: Throwable) {
                    closeSilently()
                    throw e
                }
            }
        }

        override val connection: Connection by connectionLazy

        override fun commit() {
            if (connectionLazy.isInitialized()) {
                connection.commit()
            }
        }

        override fun rollback() {
            if (connectionLazy.isInitialized()) {
                connection.rollback()
            }
        }

        override fun close() {
            try {
                if (connectionLazy.isInitialized() && !connection.isClosed) {
                    connection.closeSilently()
                }
            } finally {
                threadLocal.remove()
            }
        }

        @Suppress("SwallowedException")
        private fun Connection.closeSilently() {
            try {
                if (originAutoCommit) {
                    autoCommit = true
                }
            } catch (_: Throwable) {
            } finally {
                try {
                    close()
                } catch (_: Throwable) {
                }
            }
        }
    }
}
