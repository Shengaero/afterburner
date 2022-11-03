package io.github.shengaero.afterburner.generator

import io.github.shengaero.afterburner.annotations.RequestHandler
import io.github.shengaero.afterburner.annotations.RouteHandler
import io.github.shengaero.afterburner.annotations.param.ParamHandler
import io.github.shengaero.afterburner.annotations.param.ParamProvider
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions

private typealias HandlerFunction<T, R> = suspend PipelineContext<*, ApplicationCall>.(T) -> R

class DefaultRouteGenerator(shouldCache: Boolean) : RouteGenerator {
    private val paramHandlerFactoryCache = hashMapOf<String, ParamHandler<Any?>>().takeIf { shouldCache }

    override fun generateOn(route: Route, instance: Any) {
        val instanceKClass = instance::class
        val annotation = instanceKClass.findAnnotation<RouteHandler>()
            ?: throw IllegalStateException("No @ServerRoute present on ${instanceKClass.qualifiedName}")
        val handlers = instanceKClass.functions
            .mapNotNull { fn -> fn.findAnnotation<RequestHandler>()?.let { fn to it } }

        route.route(annotation.path) {
            handlers.forEach { (fn, requestHandler) -> generateHandlerOn(this@route, instance, fn, requestHandler) }
        }
    }

    private fun generateHandlerOn(route: Route, instance: Any, fn: KFunction<*>, requestHandler: RequestHandler) {
        // we're going to reuse this, so we create it once
        val instanceHandler: ParamHandler<Any> = { instance }
        val paramHandlers = fn.parameters.mapIndexed map@{ index, param ->
            // first is always instance
            if(index == 0) return@map instanceHandler
            // parse param
            return@map parseParamIntoHandler(param)
        }
        val path = requestHandler.path
        // wrap function up
        val reflected = wrapReflectedFunction<Unit>(fn, paramHandlers)
        // create route handle
        route.route(path, requestHandler.method.ktorMethod) { handle(reflected) }
    }

    private fun parseParamIntoHandler(param: KParameter): ParamHandler<Any?> {
        // find the first not null, or null if none were found
        val (annotation, provider) = param.annotations.firstNotNullOfOrNull first@{ a ->
            a.annotationClass.findAnnotation<ParamProvider>()?.let { p -> return@first a to p }
        } ?: throw IllegalStateException("No valid factory annotations present on param: $param")

        // if we have one cached, use that one
        paramHandlerFactoryCache?.get(provider.name)?.let { return it }

        // otherwise, we need to actually start up the annotation's factory:
        val companion = annotation.annotationClass.companionObject?.objectInstance

        // check if it's not null, if it is there was no companion object
        checkNotNull(companion) { "No companion object located on ParamProvider annotation: @$annotation" }

        // check if it is star typed factory
        check(companion is ParamProvider.Factory<*, *>) {
            "Companion object does not implement ParamProvider.Factory on annotation: @$annotation"
        }

        @Suppress("UNCHECKED_CAST")
        val handler = (companion as ParamProvider.Factory<Annotation, Any>).generate(param, annotation)

        // add to cache
        paramHandlerFactoryCache?.set(provider.name, handler)
        return handler
    }

    private fun <T> wrapReflectedFunction(
        fn: KFunction<*>,
        paramHandlers: List<ParamHandler<*>>
    ): HandlerFunction<T, Unit> {
        if(fn.isSuspend)
            return { fn.callSuspend(*paramHandlers.map { it(this) }.toTypedArray()) }
        return { fn.call(*paramHandlers.map { it(this) }.toTypedArray()) }
    }
}
