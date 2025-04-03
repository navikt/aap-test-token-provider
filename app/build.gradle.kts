import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val ktorVersion = "3.1.2"

plugins {
    kotlin("jvm") version "2.1.20"
    id("io.ktor.plugin") version "3.1.2"
    application
}

repositories {
    mavenCentral()
    maven("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
}

val aapLibVersion = "5.0.76"

dependencies {
    implementation("com.github.navikt.aap-libs:ktor-auth:$aapLibVersion")

    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")


    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    constraints {
        implementation("io.netty:netty-common:4.1.119.Final")
    }
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-client-java:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.3")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    implementation("com.nimbusds:nimbus-jose-jwt:10.1")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")
    implementation("io.ktor:ktor-server-call-logging-jvm:3.1.2")

    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
}

application {
    // Define the main class for the application.
    mainClass.set("tokenprovider.AppKt")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

kotlin.sourceSets["main"].kotlin.srcDirs("main")
kotlin.sourceSets["test"].kotlin.srcDirs("test")
sourceSets["main"].resources.srcDirs("main")
sourceSets["test"].resources.srcDirs("test")