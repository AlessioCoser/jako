plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.72"
    `java-library`
}

repositories {
    jcenter()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("net.wuerl.kotlin:assertj-core-kotlin:0.2.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.6.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.6.1")
    testImplementation("org.testcontainers:junit-jupiter:1.14.0")
    testImplementation("org.postgresql:postgresql:42.2.6")
}

tasks {
    named<Test>("test") {
        useJUnitPlatform()
    }
}
