package demo

import java.io.File
import org.slf4j.LoggerFactory
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName


class Main {
}

fun main() {
    // this works as expected
    // genericContainer()

    // this does not work
    dockerCompose()
}

private fun dockerCompose() {
    val container =
        DockerComposeContainer<Nothing>(File("src/main/resources/compose-test.yml")).apply {
            withLogConsumer("nginx_1", Slf4jLogConsumer(LoggerFactory.getLogger("docker.nginx")))
            waitingFor("nginx_1", Wait.forLogMessage(".*start worker processes.", 1))
        }

    container.start()
    println("started successfully")
    container.stop()
    println("stopped")
    container.start() // timeout here!
    println("restarted successfully")
    container.stop()
}

private fun genericContainer() {
    val container = GenericContainer(DockerImageName.parse("nginx"))
        .withLogConsumer(Slf4jLogConsumer(LoggerFactory.getLogger("docker.nginx")))
        .waitingFor(Wait.forLogMessage(".*start worker processes.", 1))

    container.start()
    println("started successfully!")
    container.stop()
    println("stopped")
    container.start()
    println("started successfully!")
    container.stop()
}