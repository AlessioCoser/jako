package jako.database.utils

import java.sql.Connection

class MockedConnectionGenerator {
    val initiatedConnections = mutableListOf<FakeConnection>()
    fun connector(): () -> Connection {
        return {
            val fakeConnection = FakeConnection()
            initiatedConnections.add(fakeConnection)
            fakeConnection
        }
    }
}
