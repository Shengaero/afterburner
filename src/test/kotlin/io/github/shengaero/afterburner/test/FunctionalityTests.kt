package io.github.shengaero.afterburner.test

import io.github.shengaero.afterburner.Afterburner
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class FunctionalityTests {
    @Test
    fun `test registered route runs`() = testApplication {
        application {
            install(Routing)
            install(Afterburner) {
                handler { call -> call.respond("Hello") }
            }
        }
        assertEquals(
            expected = "Hello",
            actual = client.get("/test").body()
        )
    }
}
