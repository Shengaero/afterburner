package io.github.shengaero.afterburner.annotations.param

import io.ktor.server.application.*

@ParamProvider("RouteCall")
@Retention(AnnotationRetention.RUNTIME)
annotation class RouteCall {
    companion object : ParamProvider.SimpleFactory<RouteCall, ApplicationCall> {
        private val GET_CALL: ParamHandler<ApplicationCall> = { this.call }
        override fun generate(annotation: RouteCall): ParamHandler<ApplicationCall> = GET_CALL
    }
}
