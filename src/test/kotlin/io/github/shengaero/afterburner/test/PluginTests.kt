package io.github.shengaero.afterburner.test

import io.github.shengaero.afterburner.Afterburner
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertFails

class PluginTests {
    @Test
    fun `test plugin successfully registers`() = testApplication {
        application {
            install(Routing)
            install(Afterburner)
            assert(Afterburner.key in pluginRegistry)
        }
    }

    @Test
    fun `test plugin fails to register when Routing not installed`() = testApplication {
        application {
            assertFails { install(Afterburner) }
        }
    }

    @Test
    fun `test plugin registers with handler`() = testApplication {
        application {
            install(Routing)
            install(Afterburner) {
                handler { call -> call.respond("Hello") }
            }
            assert(pluginRegistry[Afterburner.key].routeHandlers.isNotEmpty())
        }
    }
}
