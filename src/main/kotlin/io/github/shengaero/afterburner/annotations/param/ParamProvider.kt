package io.github.shengaero.afterburner.annotations.param

import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import kotlin.reflect.KParameter

typealias ParamHandler<T> = suspend PipelineContext<*, ApplicationCall>.() -> T

@Retention(AnnotationRetention.RUNTIME)
annotation class ParamProvider(val name: String) {
    interface SimpleFactory<A : Annotation, T> : Factory<A, T> {
        fun generate(annotation: A): ParamHandler<T>
        override fun generate(param: KParameter, annotation: A): ParamHandler<T> = generate(annotation)
    }

    interface Factory<A : Annotation, T> {
        fun generate(param: KParameter, annotation: A): ParamHandler<T>
    }
}
