import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String = "${properties["ktor_version"]}"
val kotlinVersion: String = "${properties["kotlin_version"]}"

plugins {
  `java-library`
  kotlin("jvm") version "1.7.20"
}

group = "io.github.shengaero"
version = "0.1.0"

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("reflect"))
  implementation("io.ktor", "ktor-server-core-jvm", ktorVersion)

  testImplementation(kotlin("test"))
  testImplementation("io.ktor", "ktor-server-test-host", ktorVersion)
  testImplementation("io.ktor", "ktor-server-netty-jvm", ktorVersion)
  testImplementation("ch.qos.logback", "logback-classic", "1.4.4")
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}
