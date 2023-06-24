package jako.integration

import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.containers.wait.strategy.WaitStrategy
import org.testcontainers.utility.MountableFile.forClasspathResource
import java.time.Duration

class ContainerMysql(
    user: String = "user",
    password: String = "password",
    port: Int = 3306,
    reuse: Boolean = true,
    waitStrategy: () -> WaitStrategy = { defaultWaitStrategy() }
) : FixedHostPortGenericContainer<Nothing>("mysql:8.0.33") {
    init {
        withEnv("MYSQL_ROOT_PASSWORD", password)
        withEnv("MYSQL_USER", user)
        withEnv("MYSQL_PASSWORD", password)
        withEnv("MYSQL_DATABASE", "tests")
        withCopyFileToContainer(forClasspathResource("db_mysql"), "/docker-entrypoint-initdb.d")
        withFixedExposedPort(port, 3306)
        withReuse(reuse)
        waitingFor(waitStrategy())
    }

    companion object {
        fun defaultWaitStrategy(): WaitStrategy = LogMessageWaitStrategy()
            .withRegEx(".*mysqld: ready for connections.*port: 3306.*\\s")
    }
}