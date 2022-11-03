package io.github.shengaero.afterburner.annotations.param

import io.ktor.server.application.*
import io.ktor.util.pipeline.*

@ParamProvider("URLPath")
@Retention(AnnotationRetention.RUNTIME)
annotation class URLPath(val name: String) {
    companion object : ParamProvider.SimpleFactory<URLPath, String> {
        override fun generate(annotation: URLPath): ParamHandler<String> = {
            requireNotNull(call.parameters[annotation.name]) {
                "URL parameter with name \"${annotation.name}\" not found!"
            }
        }
    }
}
