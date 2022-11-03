package io.github.shengaero.afterburner.annotations

import io.ktor.http.*

@Retention(AnnotationRetention.RUNTIME)
annotation class RouteHandler(val path: String)
