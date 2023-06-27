import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.20"
    `java-library`
    `maven-publish`
}

version = "0.2.0"
group = "com.alessiocoser"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.postgresql:postgresql:42.6.0")
    testImplementation("mysql:mysql-connector-java:8.0.15")
    testImplementation("net.wuerl.kotlin:assertj-core-kotlin:0.2.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.3")
    testImplementation("org.testcontainers:junit-jupiter:1.18.3")
    testImplementation("org.testcontainers:testcontainers:1.18.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.9.3")
}

tasks {
    named<Test>("test") {
        useJUnitPlatform()
    }
}

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("${buildDir}/publishing-repository")
        }
    }
}

val new: String by project
tasks.register("release") {
    doFirst {
        val current = version.toString()
        val next = project.getProperty("new")?.toString()

        if (next == null || next.isNotGreaterThan(current)) {
            println("Invalid new version: `$next`. New version must be present and higher to current version `$current`\n")
            println("USAGE:")
            println("gradle release -Pnew=[>$current]")
            return@doFirst
        }

        exec("sed", "-i", "", "-e", "s|'com.github.AlessioCoser:jako:$current'|'com.github.AlessioCoser:jako:$next'|g", "README.md")
        exec("sed", "-i", "", "-e", "s|<version>$current</version>|<version>$next</version>|g", "README.md")
        exec("sed", "-i", "", "-e", "s|version = \"$current\"|version = \"$next\"|g", "build.gradle.kts")
        exec("git", "commit", "-am", "RELEASE $next")
        exec("git", "tag", next)
        exec("git", "push", "--tags", "origin", "main")
        println("New version `$next` released")
    }
}

fun String.isNotGreaterThan(other: String): Boolean {
    return !isGreaterThan(other)
}

fun String.isGreaterThan(other: String): Boolean {
    val (newMajor, newMinor, newPatch) = split(".").toList()
    val (major, minor, patch) = other.split(".").toList()
    return (newMajor.toInt() > major.toInt()) ||
            (newMajor.toInt() == major.toInt() && newMinor.toInt() > minor.toInt()) ||
            (newMajor.toInt() == major.toInt() && newMinor.toInt() == minor.toInt() && newPatch.toInt() > patch.toInt())
}

fun Project.getProperty(name: String): Any? {
    return try { return properties[name] } catch (t: Throwable) { null }
}

fun exec(vararg parts: String) {
    val process = Runtime.getRuntime().exec(parts)
    val err = process.errorStream.reader(Charsets.UTF_8).readLines()
    if (err.isNotEmpty()) {
        println("Error on command: $parts")
        err.forEach { println(it) }
    }
}