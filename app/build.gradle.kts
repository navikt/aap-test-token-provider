import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val ktorVersion = "3.4.0"

plugins {
    id("aap.conventions")
    kotlin("jvm")
    id("io.ktor.plugin") version "3.4.0"
    application
}

dependencies {
    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")


    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-client-java:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.21.0")
    implementation("ch.qos.logback:logback-classic:1.5.28")
    implementation("com.nimbusds:nimbus-jose-jwt:10.7")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("net.logstash.logback:logstash-logback-encoder:9.0")
    implementation("io.ktor:ktor-server-call-logging-jvm:3.4.0")

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
}

application {
    // Define the main class for the application.
    mainClass.set("tokenprovider.AppKt")
}
