package io.github.shengaero.afterburner.generator

import io.ktor.server.routing.*

interface RouteGenerator {
    fun generateOn(route: Route, instance: Any)
}
