import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    `java-library`
    `maven-publish`
}

version = "0.0.13"
group = "com.alessiocoser"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.postgresql:postgresql:42.3.3")
    testImplementation("net.wuerl.kotlin:assertj-core-kotlin:0.2.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("org.testcontainers:junit-jupiter:1.16.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.8.2")
}

tasks {
    named<Test>("test") {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
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
        val newVersion = getProperty("new")?.toString()

        if (newVersion == null || isNotNewVersion(newVersion)) {
            println("Invalid new version: `$newVersion`. New version must be present and higher to current version `$version`\n")
            println("USAGE:")
            println("gradle release -Pnew=[>$version]")
            return@doFirst
        }

        exec("sed", "-i", "-e", "s|'com.github.AlessioCoser:jako:.*'|'com.github.AlessioCoser:jako:$newVersion'|g", "README.md")
        exec("sed", "-i", "-e", "s|<version>.*</version>|<version>$newVersion</version>|g", "README.md")
        exec("sed", "-i", "-e", "s|version = \"[0-9]*\\.[0-9]*\\.[0-9]*\"|version = \"$newVersion\"|g", "build.gradle.kts")
        exec("git", "commit", "-am", "TAG $newVersion")
        exec("git", "tag", newVersion)
        exec("git", "push", "--tags", "origin", "main")

        println(newVersion)
    }
}

fun Task.isNotNewVersion(newVersion: String): Boolean {
    val (newMajor, newMinor, newPatch) = newVersion.split(".").toList()
    val (major, minor, patch) = version.toString().split(".").toList()
    return (newMajor.toInt() < major.toInt()) ||
            (newMajor.toInt() == major.toInt() && newMinor.toInt() < minor.toInt()) ||
            (newMajor.toInt() == major.toInt() && newMinor.toInt() == minor.toInt() && newPatch.toInt() <= patch.toInt())
}

fun Task.getProperty(name: String): Any? {
    return try {
        return project.properties[name]
    } catch (t: Throwable) {
        null
    }
}

fun exec(vararg parts: String) {
    println(parts.joinToString(" "))
    val process = Runtime.getRuntime().exec(parts)
    val out = process.inputStream.reader(Charsets.UTF_8).readLines()
    val err = process.errorStream.reader(Charsets.UTF_8).readLines()
    println(err)
}