@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter", "unused")

package io.github.shengaero.afterburner

import io.github.shengaero.afterburner.generator.DefaultRouteGenerator
import io.github.shengaero.afterburner.generator.RouteGenerator
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.*

class Afterburner(configuration: Configuration, private val routing: Routing) {
    val routeHandlers = configuration.routeHandlers

    class Configuration {
        val routeHandlers = mutableListOf<Any>()
        var shouldCache: Boolean = true

        // somewhat lazy
        private var _generator: RouteGenerator? = null
        var generator: RouteGenerator
            get() = _generator ?: DefaultRouteGenerator(shouldCache).also { _generator = it }
            set(value) {
                _generator = value
            }

        operator fun plusAssign(any: Any) {
            routeHandlers += any
        }
    }

    init {
        configuration.routeHandlers.forEach {
            configuration.generator.generateOn(routing, it)
        }
    }

    companion object Plugin : BaseRouteScopedPlugin<Configuration, Afterburner> {
        override val key = AttributeKey<Afterburner>("Afterburner")
        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): Afterburner {
            // create and apply configuration
            val configuration = Configuration().apply(configure)
            // check that routing is installed
            check(Routing.key in pipeline.pluginRegistry) { "Routing plugin not installed!" }
            // get routing
            val routing = pipeline.pluginRegistry[Routing.key]
            // return plugin
            return Afterburner(configuration, routing)
        }
    }
}
