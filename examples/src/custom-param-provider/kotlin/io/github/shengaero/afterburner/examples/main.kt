package io.github.shengaero.afterburner.examples

import io.github.shengaero.afterburner.Afterburner
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.slf4j.event.Level

fun main() {
    embeddedServer(Netty, 8080, "localhost") {
        install(CallLogging) {
            level = Level.INFO
            filter { call -> call.request.path().startsWith("/") }
        }

        install(ContentNegotiation) { json() }

        install(Routing)

        install(Afterburner) {
            this += PostRoutes()
        }
    }.start(wait = true)
}
