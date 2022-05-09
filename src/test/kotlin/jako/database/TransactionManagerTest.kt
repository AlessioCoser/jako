package jako.database

import jako.database.utils.MockedConnectionGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class TransactionManagerTest {

    @Test
    fun `useTransaction does not instantiate a connection when not used`() {
        val generator = MockedConnectionGenerator()
        val manager = TransactionManager(generator.connector())

        manager.useTransaction { }

        assertThat(generator.initiatedConnections.size).isEqualTo(0)
    }

    @Test
    fun `useTransaction autoCommit is disabled in transaction and re_enabled when done`() {
        val generator = MockedConnectionGenerator()
        val manager = TransactionManager(generator.connector())

        manager.useTransaction { transaction ->
            assertThat(transaction.connection.autoCommit).isFalse()
        }

        val connection = generator.initiatedConnections.first()
        assertThat(connection.autoCommit).isTrue()
    }

    @Test
    fun `useTransaction autoCommit is disabled in transaction and re_enabled when exception thrown`() {
        val generator = MockedConnectionGenerator()
        val manager = TransactionManager(generator.connector())

        assertThrows(RuntimeException::class.java) {
            manager.useTransaction { transaction ->
                assertThat(transaction.connection.autoCommit).isFalse()
                throw RuntimeException()
            }
        }

        val connection = generator.initiatedConnections.first()
        assertThat(connection.autoCommit).isTrue()
    }

    @Test
    fun `nested useTransaction use the same transaction`() {
        val generator = MockedConnectionGenerator()
        val manager = TransactionManager(generator.connector())

        manager.useTransaction { outer ->
            manager.useTransaction { inner ->
                assertThat(inner.connection).isSameAs(outer.connection)
            }
        }

        assertThat(generator.initiatedConnections.size).isEqualTo(1)
    }

    @Test
    fun `nested useTransaction commit changes when both done and no errors occured`() {
        val generator = MockedConnectionGenerator()
        val manager = TransactionManager(generator.connector())

        manager.useTransaction { outer ->
            manager.useTransaction { inner ->
                assertThat(inner.connection).isSameAs(outer.connection)
            }
        }

        val connection = generator.initiatedConnections.first()
        assertThat(connection.commitCalledTimes).isEqualTo(1)
        assertThat(connection.rollbackCalledTimes).isEqualTo(0)
    }

    @Test
    fun `nested useTransaction rollback changes when some error occured`() {
        val generator = MockedConnectionGenerator()
        val manager = TransactionManager(generator.connector())

        assertThrows(RuntimeException::class.java) {
            manager.useTransaction { outer ->
                manager.useTransaction { inner ->
                    assertThat(inner.connection).isSameAs(outer.connection)
                    throw RuntimeException()
                }
            }
        }

        val connection = generator.initiatedConnections.first()
        assertThat(connection.commitCalledTimes).isEqualTo(0)
        assertThat(connection.rollbackCalledTimes).isEqualTo(1)
    }

    @Test
    fun `nested useTransaction close connection when both done`() {
        val generator = MockedConnectionGenerator()
        val manager = TransactionManager(generator.connector())

        manager.useTransaction { outer ->
            manager.useTransaction { inner ->
                assertThat(inner.connection).isSameAs(outer.connection)
            }
        }

        val connection = generator.initiatedConnections.first()
        assertThat(connection.isClosed).isTrue()
        assertThat(connection.closeCalledTimes).isEqualTo(1)
    }

    @Test
    fun `nested useTransaction close connection when some error occured`() {
        val generator = MockedConnectionGenerator()
        val manager = TransactionManager(generator.connector())

        assertThrows(RuntimeException::class.java) {
            manager.useTransaction { outer ->
                manager.useTransaction { inner ->
                    assertThat(inner.connection).isSameAs(outer.connection)
                    throw RuntimeException()
                }
            }
        }

        val connection = generator.initiatedConnections.first()
        assertThat(connection.isClosed).isTrue()
        assertThat(connection.closeCalledTimes).isEqualTo(1)
    }

    @Test
    fun `useConnection always instantiate a connection`() {
        val generator = MockedConnectionGenerator()
        val manager = TransactionManager(generator.connector())

        manager.useConnection { }

        assertThat(generator.initiatedConnections.size).isEqualTo(1)
    }

    @Test
    fun `useConnection by default maintains autoCommit = true and does not call commit or callback`() {
        val generator = MockedConnectionGenerator()
        val manager = TransactionManager(generator.connector())

        manager.useConnection { conn ->
            assertThat(conn.autoCommit).isTrue()
            assertThat(generator.initiatedConnections.first().autoCommit).isTrue()
        }

        val connection = generator.initiatedConnections.first()
        assertThat(connection.autoCommit).isTrue()
        assertThat(connection.commitCalledTimes).isEqualTo(0)
        assertThat(connection.rollbackCalledTimes).isEqualTo(0)
    }

    @Test
    fun `useConnection closes the connection`() {
        val generator = MockedConnectionGenerator()
        val manager = TransactionManager(generator.connector())

        manager.useConnection { conn ->
            assertThat(conn).isSameAs(generator.initiatedConnections.first())
        }

        val connection = generator.initiatedConnections.first()
        assertThat(connection.isClosed).isTrue()
        assertThat(connection.closeCalledTimes).isEqualTo(1)
    }

    @Test
    fun `useConnection closes the connection when exception thrown`() {
        val generator = MockedConnectionGenerator()
        val manager = TransactionManager(generator.connector())

        assertThrows(RuntimeException::class.java) {
            manager.useConnection { connection ->
                assertThat(connection.isClosed).isFalse()
                throw RuntimeException()
            }
        }

        val connection = generator.initiatedConnections.first()
        assertThat(connection.isClosed).isTrue()
    }

    @Test
    fun `useConnection inside useTransaction uses the same connection`() {
        val generator = MockedConnectionGenerator()
        val manager = TransactionManager(generator.connector())

        manager.useTransaction { transaction ->
            manager.useConnection { connection ->
                assertThat(connection).isSameAs(transaction.connection)
            }
        }

        assertThat(generator.initiatedConnections.size).isEqualTo(1)
    }

    @Test
    fun `useConnection inside useTransaction does not close the connection`() {
        val generator = MockedConnectionGenerator()
        val manager = TransactionManager(generator.connector())

        manager.useTransaction { transaction ->
            manager.useConnection { connection ->
                assertThat(connection).isSameAs(transaction.connection)
            }
            assertThat(transaction.connection.isClosed).isFalse()
        }
    }

    @Test
    fun `useConnection inside useTransaction maintains autoCommit = false`() {
        val generator = MockedConnectionGenerator()
        val manager = TransactionManager(generator.connector())

        manager.useTransaction {
            manager.useConnection { conn ->
                assertThat(conn.autoCommit).isFalse()
                assertThat(generator.initiatedConnections.first().autoCommit).isFalse()
            }
        }
    }
}
