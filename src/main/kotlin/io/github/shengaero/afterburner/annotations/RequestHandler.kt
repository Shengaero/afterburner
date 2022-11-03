package io.github.shengaero.afterburner.annotations

import io.ktor.http.*

@Retention(AnnotationRetention.RUNTIME)
annotation class RequestHandler(val method: Method, val path: String = "") {
    enum class Method(val ktorMethod: HttpMethod) {
        GET(HttpMethod.Get),
        POST(HttpMethod.Post),
        PUT(HttpMethod.Put),
        DELETE(HttpMethod.Delete)
    }
}
