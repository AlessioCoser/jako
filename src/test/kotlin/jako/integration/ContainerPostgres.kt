package jako.integration

import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.utility.MountableFile.forClasspathResource

class ContainerPostgres(
    user: String = "user",
    password: String = "password",
    port: Int = 5432,
    reuse: Boolean = true,
    waitStrategy: () -> LogMessageWaitStrategy = { defaultWaitStrategy() }
) : FixedHostPortGenericContainer<Nothing>("postgres:11") {
    init {
        withEnv("POSTGRES_USER", user)
        withEnv("POSTGRES_PASSWORD", password)
        withCopyFileToContainer(forClasspathResource("db"), "/docker-entrypoint-initdb.d")
        withFixedExposedPort(port, 5432)
        withReuse(reuse)
        waitingFor(waitStrategy())
    }

    companion object {
        fun defaultWaitStrategy(): LogMessageWaitStrategy = LogMessageWaitStrategy()
            .withRegEx(".*database system is ready to accept connections.*\\s")
            .withTimes(2)
    }
}