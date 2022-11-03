import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String = "${properties["ktor_version"]}"
val kotlinVersion: String = "${properties["kotlin_version"]}"

plugins {
    `java-library`
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
}

group = "io.github.shengaero"
version = "0.1.0"

sourceSets {
    create("basic-server") {
        java.srcDir("src/basic-server/java")
        kotlin.srcDir("src/basic-server/kotlin")
        compileClasspath += main.get().compileClasspath
        runtimeClasspath += main.get().runtimeClasspath
    }
    create("custom-param-provider") {
        java.srcDir("src/custom-param-provider/java")
        kotlin.srcDir("src/custom-param-provider/kotlin")
        compileClasspath += main.get().compileClasspath
        runtimeClasspath += main.get().runtimeClasspath
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("ch.qos.logback:logback-classic:1.4.4")
    implementation("io.ktor", "ktor-server", ktorVersion)
    implementation("io.ktor", "ktor-server-core-jvm", ktorVersion)
    implementation("io.ktor", "ktor-server-netty-jvm", ktorVersion)
    implementation("io.ktor", "ktor-serialization-kotlinx-json", ktorVersion)
    implementation(rootProject)
    implementation("io.ktor:ktor-server-compression-jvm:2.1.3")
    implementation("io.ktor:ktor-server-cors-jvm:2.1.3")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.1.3")

    testImplementation(kotlin("test"))
    testImplementation("io.ktor", "ktor-server-netty-jvm", ktorVersion)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}