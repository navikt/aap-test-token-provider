import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

repositories {
    maven("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.21")
    implementation("dev.detekt:detekt-gradle-plugin:2.0.0-alpha.3")
}
