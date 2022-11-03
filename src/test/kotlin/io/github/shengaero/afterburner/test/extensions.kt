package io.github.shengaero.afterburner.test

import io.github.shengaero.afterburner.Afterburner
import io.github.shengaero.afterburner.annotations.RequestHandler
import io.github.shengaero.afterburner.annotations.RouteHandler
import io.github.shengaero.afterburner.annotations.param.RouteCall
import io.ktor.server.application.*

fun Afterburner.Configuration.handler(function: suspend (call: ApplicationCall) -> Unit) {
    this += BasicFunctionRouteHandler(function)
}

@RouteHandler("/test")
class BasicFunctionRouteHandler(private val function: suspend (ApplicationCall) -> Unit) {
    @RequestHandler(RequestHandler.Method.GET)
    suspend fun getTest(@RouteCall call: ApplicationCall) {
        function(call)
    }
}
