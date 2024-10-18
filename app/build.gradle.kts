import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion = "3.0.0"

plugins {
    kotlin("jvm") version "2.0.21"
    id("io.ktor.plugin") version "3.0.0"
    application
}

repositories {
    mavenCentral()
    maven("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
}

val aapLibVersion = "3.7.162"

dependencies {
    implementation("com.github.navikt.aap-libs:ktor-auth-maskinporten:$aapLibVersion")
    implementation("com.github.navikt.aap-libs:ktor-utils:$aapLibVersion")

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
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.0")
    implementation("ch.qos.logback:logback-classic:1.5.11")
    implementation("com.nimbusds:nimbus-jose-jwt:9.41.2")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")
    implementation("io.ktor:ktor-server-call-logging-jvm:3.0.0")

    testImplementation(kotlin("test"))
}

application {
    // Define the main class for the application.
    mainClass.set("tokenprovider.AppKt")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "21"
    }
    withType<Test> {
        useJUnitPlatform()
    }
}

kotlin.sourceSets["main"].kotlin.srcDirs("main")
kotlin.sourceSets["test"].kotlin.srcDirs("test")
sourceSets["main"].resources.srcDirs("main")
sourceSets["test"].resources.srcDirs("test")